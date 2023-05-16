package rubber.dutch.hat.domain.model

import jakarta.persistence.*
import org.hibernate.annotations.JdbcTypeCode
import org.hibernate.type.SqlTypes
import rubber.dutch.hat.domain.exception.*
import java.time.Instant

@Entity
@Table(name = "game")
@SuppressWarnings("LongParameterList")
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

    @Column(name = "start_time", nullable = false)
    val startTime: Instant = Instant.now(),

    @Column(name = "end_time")
    var endTime: Instant? = null,

    @Enumerated(value = EnumType.STRING)
    @Column(name = "status")
    var status: GameStatus = GameStatus.NEW,

    @OneToMany(fetch = FetchType.LAZY, cascade = [CascadeType.ALL])
    @JoinColumn(name = "game_id")
    var rounds: MutableList<Round> = mutableListOf()
) {

    fun addPlayer(userId: UserId): Player {
        findPlayer(userId)?.let { return it }

        if (players.size >= MAX_PLAYERS_COUNT) {
            throw PlayersLimitExceededException()
        }
        val player = Player(
            id = PlayerInternalId(),
            userId = userId,
            gameId = id,
            status = PlayerStatus.NEW,
            role = PlayerRole.PLAYER,
        )
        players.add(player)
        return player
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
                authorId = player.id,
                status = WordInGameStatus.AVAILABLE
            )
        }
        player.words.addAll(wordsInGame)

        if (player.words.size == config.wordsPerPlayer) {
            player.status = PlayerStatus.READY
        }
    }

    private fun findPlayer(userId: UserId): Player? {
        return players.firstOrNull { it.userId == userId }
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

    fun getNewWord(): WordInGame? {
        return words.filter { it.status == WordInGameStatus.AVAILABLE }.randomOrNull()
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

    fun finish() {
        status = GameStatus.FINISHED
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
