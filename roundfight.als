module roundfight

/* Modelagem em Alloy do sistema RoundFight */

sig Arena {
  jogadores: set Jogador
}

sig Jogador {}

fact LimiteJogadores {
	all a:Arena | #a.jogadores = 4
	all j:Jogador, a:Arena | j in a.jogadores => j ! in (Arena - a).jogadores
}

pred Partida1 {
	some Arena
}

run Partida1 for 10
