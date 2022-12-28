package rubber.dutch.hat.domain

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.bind.ConstructorBinding

@ConfigurationProperties("app.game")
class GameConfigProperties @ConstructorBinding constructor(val maxPlayers: Int)