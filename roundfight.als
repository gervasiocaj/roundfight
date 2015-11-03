module roundfight

/* Modelagem em Alloy do sistema RoundFight */

abstract sig Jogador {}
sig JogadorReal, JogadorAI extends Jogador {}

sig Arena { jogadores: set Jogador }

sig Leaderboards { jogadores: set Jogador }

fact LimiteJogadores {
	one Leaderboards
	all j:JogadorAI | j in Arena.jogadores // os jogadores com AI estao sempre em partidas
	some j:JogadorReal | j in Leaderboards.jogadores
	all j:Jogador, a:Arena | j in a.jogadores => j ! in (Arena - a).jogadores // um jogador esta em somente uma partida ao mesmo tempo
	all a:Arena | #a.jogadores >= 2 // uma arena tem entre 2 e 4 jogadores
	all a:Arena | one jRealHost:JogadorReal | jRealHost in a.jogadores // uma partida contem um jogador real como host
}

pred Partida {
	some Arena
}

run Partida for 10
