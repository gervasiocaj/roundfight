module roundfight

/* Modelagem em Alloy do sistema RoundFight */

sig Arena {
	jogadores: set Jogador
}

abstract sig Jogador {}
sig JogadorReal, JogadorAI extends Jogador {}

fact LimiteJogadores {
	// falta declarar que há pelo menos um jogador real em cada partida
	all a:Arena | #a.jogadores <= 4 and #a.jogadores >= 1
	all j:Jogador, a:Arena | j in a.jogadores => j ! in (Arena - a).jogadores
}

pred Partida1 {
	some Arena
}

run Partida1 for 10
