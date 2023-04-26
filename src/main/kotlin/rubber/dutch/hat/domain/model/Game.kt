package rubber.dutch.hat.domain.model

import jakarta.persistence.*
import org.hibernate.annotations.JdbcTypeCode
import org.hibernate.type.SqlTypes
import rubber.dutch.hat.domain.exception.*

@Entity
@Table(name = "game")
class Game(
    @Id
    @Column(name = "id", nullable = false)
    val id: GameId,

    @Column(nullable = false)
    val code: String,

    @Column(name = "creator_id")
    val creatorId: UserId,

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "config")
    val config: GameConfig,

    @OneToMany(fetch = FetchType.LAZY, cascade = [CascadeType.ALL])
    @JoinColumn(name = "game_id")
    val players: MutableList<Player> = mutableListOf(),

    @OneToMany(fetch = FetchType.LAZY, cascade = [CascadeType.ALL])
    @JoinColumn(name = "game_id")
    val words: MutableList<WordInGame> = mutableListOf(),

    @Enumerated(value = EnumType.STRING)
    @Column(name = "status")
    var status: GameStatus = GameStatus.NEW,

    @OneToMany(fetch = FetchType.LAZY, cascade = [CascadeType.ALL])
    @JoinColumn(name = "game_id")
    var rounds: MutableList<Round> = mutableListOf()
) {

    fun addPlayer(userId: UserId) {
        if (isUserInGame(userId)) {
            return
        }
        if (players.size >= MAX_PLAYERS_COUNT) {
            throw PlayersLimitExceededException()
        }
        players.add(
            Player(
                id = PlayerInternalId(),
                userId = userId,
                gameId = id,
                status = PlayerStatus.NEW,
                role = PlayerRole.PLAYER,
            )
        )
    }

    fun addWordsToPlayer(userId: UserId, words: List<String>) {
        val player = players.find { it.userId == userId }
            ?: throw UserNotJoinedException()

        if (player.words.size + words.size > config.wordsPerPlayer) {
            throw WordsLimitExceededException()
        }

        val wordsInGame = words.map {
            WordInGame(
                gameId = id,
                value = it,
                authorId = player.id.internalId!!,
                status = WordInGameStatus.AVAILABLE
            )
        }
        player.words.addAll(wordsInGame)

        if (player.words.size == config.wordsPerPlayer) {
            player.status = PlayerStatus.READY
        }
    }

    private fun isUserInGame(userId: UserId): Boolean {
        return players.any { it.userId == userId }
    }

    fun getPlayerByUserId(userId: UserId): Player {
        return players.firstOrNull { it.userId == userId } ?: throw UserNotJoinedException()
    }

    private fun getPlayerByPlayerId(playerId: PlayerInternalId): Player {
        return players.firstOrNull { it.id == playerId } ?: throw PlayerNotFoundException()
    }

    fun createRound(playerId: PlayerInternalId): Round {
        if (rounds.any { it.status == Round.RoundStatus.STARTED }) {
            throw RoundStatusException()
        }

        if (!isCorrectMoveOrder(getPlayerByPlayerId(playerId))) {
            throw IncorrectMoveOrderException()
        }

        val round = Round(
            id = RoundId(),
            explainerId = playerId,
            gameId = id
        )
        rounds.add(round)
        return round
    }

    fun getNewWord(): WordInGame {
        return words.filter { it.status == WordInGameStatus.AVAILABLE }.random()
    }

    private fun getLastRound(): Round? {
        return rounds.maxByOrNull { it.startTime }
    }

    fun getCurrentRound(): Round {
        if (rounds.size == 0) {
            throw RoundNotFoundException()
        }
        val round = getLastRound()
        if (round?.status != Round.RoundStatus.STARTED) {
            throw RoundStatusException()
        }
        return round
    }

    private fun isCorrectMoveOrder(currentPlayer: Player): Boolean {
        val round = getLastRound() ?: return currentPlayer.moveOrder == 0

        val lastPlayer = getPlayerByPlayerId(round.explainerId)
        return currentPlayer.moveOrder == (lastPlayer.moveOrder + 1) % players.size
    }

    enum class GameStatus {
        NEW,
        STARTED,
        FINISHED
    }
}
