package chess;

/* Author: Yue Luo      yl1535
           Nicole Le    nl465
*/

import java.util.ArrayList;

class ReturnPiece {
	static enum PieceType {WP, WR, WN, WB, WQ, WK, 
		            BP, BR, BN, BB, BK, BQ};
	static enum PieceFile {a, b, c, d, e, f, g, h};
	
	PieceType pieceType;
	PieceFile pieceFile;
	int pieceRank;  // 1..8
	public String toString() {
		return ""+pieceFile+pieceRank+":"+pieceType;
	}
	public boolean equals(Object other) {
		if (other == null || !(other instanceof ReturnPiece)) {
			return false;
		}
		ReturnPiece otherPiece = (ReturnPiece)other;
		return pieceType == otherPiece.pieceType &&
				pieceFile == otherPiece.pieceFile &&
				pieceRank == otherPiece.pieceRank;
	}
}

class ReturnPlay {
	enum Message {ILLEGAL_MOVE, DRAW, 
				  RESIGN_BLACK_WINS, RESIGN_WHITE_WINS, 
				  CHECK, CHECKMATE_BLACK_WINS,	CHECKMATE_WHITE_WINS, 
				  STALEMATE};
	
	ArrayList<ReturnPiece> piecesOnBoard;
	Message message;
}

class ComparablePiece extends ReturnPiece{
        public boolean equals(String name, boolean ForType){
            if(ForType == true){
                PieceType temp = ReturnPiece.PieceType.WP;
                if(pieceType == temp.valueOf(name)) return true;
                else return false;
            }
            else{
                PieceFile temp2 = ReturnPiece.PieceFile.a;
                if(pieceFile == temp2.valueOf(name)) return true;
                else return false;
            }
        }
}

public class Chess {
	
	enum Player { white, black }
        static ReturnPlay RP = new ReturnPlay();
        static Player player = Player.white;
        static ReturnPiece Enpassant = new ReturnPiece();
        static boolean[] IfMove = new boolean[6];           //0:WR1, 1:WK, 2:WR2, 3:BR1, 4:BK, 5: BR2
        static boolean[] IfCheck = new boolean[2];            //0:White, 1:Black
        static boolean CastleCall = false;
        static boolean ExtraExist = false;
        static String Extra = "";
	
        public static void EnpassantSet(ReturnPiece.PieceType PT, ReturnPiece.PieceFile PF, int rk){
            Enpassant.pieceType = PT;
            Enpassant.pieceFile = PF;
            Enpassant.pieceRank = rk;
        }
        
        public static ComparablePiece createPiece(ReturnPiece.PieceType PT, ReturnPiece.PieceFile PF, int PR){
            ComparablePiece tempPiece = new ComparablePiece();
            tempPiece.pieceType = PT;
            tempPiece.pieceFile = PF;
            tempPiece.pieceRank = PR;
            return tempPiece;
        }
        
        public static ComparablePiece FindPiece(String piece){
            ComparablePiece tempPiece = new ComparablePiece();
            tempPiece.pieceFile = tempPiece.pieceFile.valueOf(piece.substring(0,1));        //Note: If input incorrect will lead to exception and immediately exit the program
            tempPiece.pieceRank = Integer.parseInt(piece.substring(1));                       //      But the instructions assume every input is correct so so be it
            return tempPiece;
        }
        
        public static ReturnPiece GetPiece(ReturnPiece TargetPosition){
            for(int i=0;i<RP.piecesOnBoard.size();i++){
                ReturnPiece temp = RP.piecesOnBoard.get(i);
                if(temp.pieceRank == TargetPosition.pieceRank && temp.pieceFile == TargetPosition.pieceFile){
                    return temp;
                }
            }
            return null;
        }
        
        public static void SetPiece(ReturnPiece onhand, ReturnPiece TargetPosition){
            for(int i=0;i<RP.piecesOnBoard.size();i++){
                ReturnPiece temp = RP.piecesOnBoard.get(i);
                if(temp.pieceRank == onhand.pieceRank && temp.pieceFile == onhand.pieceFile){
                    temp.pieceRank = TargetPosition.pieceRank;
                    temp.pieceFile = TargetPosition.pieceFile;
                    RP.piecesOnBoard.set(i,temp);
                    return;
                }
            }
        }
        
        public static void SetPiece(ReturnPiece TargetPosition){
            for(int i=0;i<RP.piecesOnBoard.size();i++){
                ReturnPiece temp = RP.piecesOnBoard.get(i);
                if(temp.pieceRank == TargetPosition.pieceRank && temp.pieceFile == TargetPosition.pieceFile){
                    temp.pieceType = TargetPosition.pieceType;
                    RP.piecesOnBoard.set(i,temp);
                    return;
                }
            }
        }
        
        public static void RemovePiece(ReturnPiece TargetPosition){
            for(int i=0;i<RP.piecesOnBoard.size();i++){
                ReturnPiece temp = RP.piecesOnBoard.get(i);
                if(temp.pieceRank == TargetPosition.pieceRank && temp.pieceFile == TargetPosition.pieceFile){
                    RP.piecesOnBoard.remove(i);
                    return;
                }
            }
        }
        
        public static boolean IfWhite(ReturnPiece target){
            ReturnPiece temp = GetPiece(target);
            ComparablePiece temp2 = createPiece(target.pieceType,target.pieceFile,target.pieceRank);
            if(temp == null) return false;
            else{
                temp2.pieceType = temp.pieceType;
                if(temp2.equals("WP",true) || temp2.equals("WR",true) || temp2.equals("WN",true) || 
                        temp2.equals("WB",true) || temp2.equals("WQ",true) || temp2.equals("WK",true)) return true;
                else return false;
            }
        }

	public static ReturnPlay play(String move) {
            CastleCall = false;
            RP.message = null;
            Extra = "";
            int spaceno = move.indexOf(" ");
            if(spaceno == -1 && move.equals("resign")){
                if(player == Player.white) RP.message = RP.message.RESIGN_BLACK_WINS;
                else RP.message = RP.message.RESIGN_WHITE_WINS;
                return RP;
            }
            while(spaceno == 0){                                            //remove white spaces between, since there may be leading or trailing spaces by instruction
                move = move.substring(spaceno+1);
                spaceno = move.indexOf(" ");
            }
            String pieceno = "";
            if(spaceno != -1) pieceno = move.substring(0,spaceno);
            else pieceno = move;
            if(move.substring(spaceno+1,spaceno+2) == " "){ spaceno++; }    //same as above
            if(pieceno.equals("resign")){
                if(player == Player.white) RP.message = RP.message.RESIGN_BLACK_WINS;
                else RP.message = RP.message.RESIGN_WHITE_WINS;
                return RP;
            }
            String otherhalf = move.substring(spaceno+1,move.length());
            ExtraExist = false;                                     //This is used for testing draw requests and other third argument methods
            if(otherhalf.indexOf(" ") != -1){
                if(otherhalf.indexOf(" ") != otherhalf.length()-1){
                    Extra = otherhalf.substring(otherhalf.indexOf(" ")+1,otherhalf.length());
                    ExtraExist = true;
                    while(Extra.indexOf(" ") == 0){ Extra = Extra.substring(1); }   //same as above
                    if(Extra.indexOf(" ") != -1){ Extra = Extra.substring(0,Extra.indexOf(" ")); }  //same as above
                }
                otherhalf = otherhalf.substring(0,otherhalf.indexOf(" "));
            }
            ComparablePiece onhand = FindPiece(pieceno);
            boolean IW = IfWhite(onhand);
            if(((!IW) && (player == Player.white)) || (IW && (player == Player.black))){
                RP.message = RP.message.ILLEGAL_MOVE;
            }
            else{
                ComparablePiece TargetPosition = FindPiece(otherhalf);
                onhand.pieceType = GetPiece(onhand).pieceType;
                if(!Reachable.Reachablemove(onhand, TargetPosition)){
                    RP.message = RP.message.ILLEGAL_MOVE;
                    Reachable.Enpassantnow = 1;
                }
                else{
                    if(GetPiece(TargetPosition) != null) RemovePiece(TargetPosition);
                    if(Reachable.Enpassantnow == 3){
                        for(int i=0;i<RP.piecesOnBoard.size();i++){
                            ReturnPiece temp = RP.piecesOnBoard.get(i);
                            if(temp.pieceRank == Enpassant.pieceRank && temp.pieceFile == Enpassant.pieceFile){
                                RP.piecesOnBoard.remove(i);
                                break;
                            }
                        }
                        EnpassantSet(null, null, -1);
                        Reachable.Enpassantnow = 0;
                    }
                    SetPiece(onhand,TargetPosition);
                    if(CastleCall == true){
                        if(player == Player.white && TargetPosition.equals("c",false)){
                            ReturnPiece Targetrook = GetPiece(createPiece(null,ReturnPiece.PieceFile.a,1));
                            SetPiece(Targetrook,createPiece(null,ReturnPiece.PieceFile.d,1));
                        }
                        else if(player == Player.white && TargetPosition.equals("g",false)){
                            ReturnPiece Targetrook = GetPiece(createPiece(null,ReturnPiece.PieceFile.h,1));
                            SetPiece(Targetrook,createPiece(null,ReturnPiece.PieceFile.f,1));
                        }
                        else if(player == Player.black && TargetPosition.equals("c",false)){
                            ReturnPiece Targetrook = GetPiece(createPiece(null,ReturnPiece.PieceFile.a,8));
                            SetPiece(Targetrook,createPiece(null,ReturnPiece.PieceFile.d,8));
                        }
                        else if(player == Player.black && TargetPosition.equals("g",false)){
                            ReturnPiece Targetrook = GetPiece(createPiece(null,ReturnPiece.PieceFile.h,8));
                            SetPiece(Targetrook,createPiece(null,ReturnPiece.PieceFile.f,8));
                        }
                        if(player == Player.white) IfMove[1] = true;
                        else IfMove[4] = true;
                    }
                    if(ExtraExist == true){
                        if(Extra.equals("draw?")){
                            RP.message = RP.message.DRAW;
                        }
                        else{
                            if(player == Player.white) Extra = "W"+Extra;
                            else Extra = "B"+Extra;
                            ReturnPiece temp = GetPiece(TargetPosition);
                            temp.pieceType = Reachable.StringtoPT(Extra);
                            SetPiece(temp);
                        }
                    }
                    if(onhand.equals("WP",true) && TargetPosition.pieceRank == 8 && !ExtraExist){
                        TargetPosition.pieceType = Reachable.StringtoPT("WQ");
                        SetPiece(TargetPosition);
                    }
                    if(onhand.equals("BP",true) && TargetPosition.pieceRank == 1 && !ExtraExist){
                        TargetPosition.pieceType = Reachable.StringtoPT("BQ");
                        SetPiece(TargetPosition);
                    }
                    if(onhand.equals("WR",true) && TargetPosition.equals("a",false) && TargetPosition.pieceRank == 1) IfMove[0] = true;
                    if(onhand.equals("WR",true) && TargetPosition.equals("h",false) && TargetPosition.pieceRank == 1) IfMove[2] = true;
                    if(onhand.equals("BR",true) && TargetPosition.equals("a",false) && TargetPosition.pieceRank == 8) IfMove[3] = true;
                    if(onhand.equals("BR",true) && TargetPosition.equals("h",false) && TargetPosition.pieceRank == 8) IfMove[5] = true; //For special cases, such as promoting rooks
                    if(player == Player.white) player = Player.black;               //switch player after success executing a move
                    else player = Player.white;
                    if(CheckChecker.IfCauseCheck(player)){
                        RP.message = RP.message.CHECK;
                        if(player == Player.white) IfCheck[0] = true;
                        else IfCheck[1] = true;
                    }
                    else{
                        IfCheck[0] = false;
                        IfCheck[1] = false;
                    }
                    if(CheckmateChecker.IfCheckmate(player)){
                        if(RP.message == RP.message.CHECK){
                            if(player == Player.black) RP.message = RP.message.CHECKMATE_WHITE_WINS;
                            else RP.message = RP.message.CHECKMATE_BLACK_WINS;
                        }
                        else RP.message = RP.message.STALEMATE;
                    }
                }
            }
            return RP;
	}

	public static void start() {        //todo: Write a subclass or method that can help transform string and enum types, or other ways to lighten the load of this
            player = Player.white;
            for(int i=0;i<6;i++) IfMove[i] = false;
            IfCheck[0] = false;
            IfCheck[1] = false;
            EnpassantSet(null,null,-1);
            CastleCall = false;
            RP.piecesOnBoard = new ArrayList<ReturnPiece>();
            RP.piecesOnBoard.add(createPiece(ReturnPiece.PieceType.WR,ReturnPiece.PieceFile.a,1));
            RP.piecesOnBoard.add(createPiece(ReturnPiece.PieceType.WN,ReturnPiece.PieceFile.b,1));
            RP.piecesOnBoard.add(createPiece(ReturnPiece.PieceType.WB,ReturnPiece.PieceFile.c,1));
            RP.piecesOnBoard.add(createPiece(ReturnPiece.PieceType.WQ,ReturnPiece.PieceFile.d,1));
            RP.piecesOnBoard.add(createPiece(ReturnPiece.PieceType.WK,ReturnPiece.PieceFile.e,1));
            RP.piecesOnBoard.add(createPiece(ReturnPiece.PieceType.WB,ReturnPiece.PieceFile.f,1));
            RP.piecesOnBoard.add(createPiece(ReturnPiece.PieceType.WN,ReturnPiece.PieceFile.g,1));
            RP.piecesOnBoard.add(createPiece(ReturnPiece.PieceType.WR,ReturnPiece.PieceFile.h,1));
            RP.piecesOnBoard.add(createPiece(ReturnPiece.PieceType.WP,ReturnPiece.PieceFile.a,2));
            RP.piecesOnBoard.add(createPiece(ReturnPiece.PieceType.WP,ReturnPiece.PieceFile.b,2));
            RP.piecesOnBoard.add(createPiece(ReturnPiece.PieceType.WP,ReturnPiece.PieceFile.c,2));
            RP.piecesOnBoard.add(createPiece(ReturnPiece.PieceType.WP,ReturnPiece.PieceFile.d,2));
            RP.piecesOnBoard.add(createPiece(ReturnPiece.PieceType.WP,ReturnPiece.PieceFile.e,2));
            RP.piecesOnBoard.add(createPiece(ReturnPiece.PieceType.WP,ReturnPiece.PieceFile.f,2));
            RP.piecesOnBoard.add(createPiece(ReturnPiece.PieceType.WP,ReturnPiece.PieceFile.g,2));
            RP.piecesOnBoard.add(createPiece(ReturnPiece.PieceType.WP,ReturnPiece.PieceFile.h,2));
            RP.piecesOnBoard.add(createPiece(ReturnPiece.PieceType.BR,ReturnPiece.PieceFile.a,8));
            RP.piecesOnBoard.add(createPiece(ReturnPiece.PieceType.BN,ReturnPiece.PieceFile.b,8));
            RP.piecesOnBoard.add(createPiece(ReturnPiece.PieceType.BB,ReturnPiece.PieceFile.c,8));
            RP.piecesOnBoard.add(createPiece(ReturnPiece.PieceType.BQ,ReturnPiece.PieceFile.d,8));
            RP.piecesOnBoard.add(createPiece(ReturnPiece.PieceType.BK,ReturnPiece.PieceFile.e,8));
            RP.piecesOnBoard.add(createPiece(ReturnPiece.PieceType.BB,ReturnPiece.PieceFile.f,8));
            RP.piecesOnBoard.add(createPiece(ReturnPiece.PieceType.BN,ReturnPiece.PieceFile.g,8));
            RP.piecesOnBoard.add(createPiece(ReturnPiece.PieceType.BR,ReturnPiece.PieceFile.h,8));
            RP.piecesOnBoard.add(createPiece(ReturnPiece.PieceType.BP,ReturnPiece.PieceFile.a,7));
            RP.piecesOnBoard.add(createPiece(ReturnPiece.PieceType.BP,ReturnPiece.PieceFile.b,7));
            RP.piecesOnBoard.add(createPiece(ReturnPiece.PieceType.BP,ReturnPiece.PieceFile.c,7));
            RP.piecesOnBoard.add(createPiece(ReturnPiece.PieceType.BP,ReturnPiece.PieceFile.d,7));
            RP.piecesOnBoard.add(createPiece(ReturnPiece.PieceType.BP,ReturnPiece.PieceFile.e,7));
            RP.piecesOnBoard.add(createPiece(ReturnPiece.PieceType.BP,ReturnPiece.PieceFile.f,7));
            RP.piecesOnBoard.add(createPiece(ReturnPiece.PieceType.BP,ReturnPiece.PieceFile.g,7));
            RP.piecesOnBoard.add(createPiece(ReturnPiece.PieceType.BP,ReturnPiece.PieceFile.h,7));
	}
}
