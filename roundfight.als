module roundfight

/* Modelagem em Alloy do sistema RoundFight */

abstract sig Jogador {}
sig JogadorReal, JogadorAI extends Jogador {}

abstract sig Arena { jogadores: set Jogador }
sig ArenaOnline, ArenaLocal extends Arena {}

sig Lobby { jogadores: set JogadorReal }

fact LimiteJogadores {
	one Lobby // existe apenas um lobby
	all j:JogadorReal | not j in Arena.jogadores <=> j in Lobby.jogadores // ou o jogador real esta numa partida ou num lobby
	all j:JogadorAI | j in Arena.jogadores // os jogadores com AI estao sempre em partidas, nunca soltos ou no lobby
	all a:Arena | #a.jogadores <= 4 and #a.jogadores >= 2 // uma arena tem entre 2 e 4 jogadores
	all j:Jogador, a:Arena | j in a.jogadores => j ! in (Arena - a).jogadores // um jogador esta em somente uma partida ao mesmo tempo
	all a:Arena | some jRealHost:JogadorReal | jRealHost in a.jogadores // uma partida contem um jogador real como host
}

pred Partida {
	some Arena
}

run Partida for 10
