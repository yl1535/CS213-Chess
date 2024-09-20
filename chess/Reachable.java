package chess;

import java.util.ArrayList;
import java.lang.Math;

public class Reachable {
    static ArrayList<ComparablePiece> Paths = new ArrayList<ComparablePiece>();
    static int Enpassantnow = 0;    //0 represents false, 1 represents true, 2 represents the turn after true, 3 represents enpassant executor
    public static boolean Reachablemove(ComparablePiece onhand, ComparablePiece TargetPosition){
        if(Enpassantnow == 1) Enpassantnow ++;
        Paths = new ArrayList<ComparablePiece>();
        if(IfInPath(onhand, TargetPosition)){
            if(CheckChecker.IfCauseCheck(onhand,TargetPosition,Chess.player)) return false;
            //if(Chess.player == Chess.Player.white) Chess.IfCheck[0] = false;
            //else Chess.IfCheck[1] = false;
            if(Enpassantnow == 2){
                Chess.EnpassantSet(null,null,-1);
                Enpassantnow = 0;
            }
                            //add anything here if needed
            return true;
        }
        return false;
    }
    
    public static boolean IfInPath(ComparablePiece onhand, ComparablePiece TargetPosition){
        int handfile = PieceFileTranslator(onhand);
        int handrank = onhand.pieceRank;
        int TPfile = PieceFileTranslator(TargetPosition);
        int TPrank = TargetPosition.pieceRank;
        if(onhand.equals("WP",true)){
            if(TPrank-handrank >=3) return false;
            else if(TPrank-handrank == 2){                  //two steps ahead
                if(handrank == 2){
                    if(TPfile != handfile) return false;    //not on same column
                    else{
                        Paths.add(Chess.createPiece(null,TargetPosition.pieceFile,TPrank-1));
                        Paths.add(Chess.createPiece(null,TargetPosition.pieceFile,TPrank));
                        if(!IfExistBlocks(Paths)){
                            Enpassantnow = 1;
                            Chess.EnpassantSet(onhand.pieceType,TargetPosition.pieceFile,TPrank);
                            return true;
                        }
                        else return false;
                    }
                }
                else return false;                          //not capable of two steps ahead
            }
            else if(TPrank-handrank == 1){                                           //one step
                if(TPfile-handfile >= 2) return false;
                else if(Math.abs(TPfile-handfile) == 1){
                    if(Chess.GetPiece(TargetPosition) != null){               //normal beat
                        if(IfSelfPieceOnTarget(onhand,TargetPosition)) return false;
                        else return true;
                    }
                    else if(Enpassantnow == 2){                              //En passant   //requires further test after moving section is finished
                        ReturnPiece temp = Chess.createPiece(Chess.Enpassant.pieceType,Chess.Enpassant.pieceFile,Chess.Enpassant.pieceRank+1);
                        TargetPosition.pieceType = Chess.Enpassant.pieceType;
                        if(temp.equals(TargetPosition)){
                            Enpassantnow = 3;
                            return true;
                        }
                        else return false;
                    }
                    else return false;
                }
                else{
                    Paths.add(Chess.createPiece(null,TargetPosition.pieceFile,TPrank));
                    if(IfExistBlocks(Paths)) return false;
                    else return true;
                }
            }
            else return false;
        }
        else if(onhand.equals("BP",true)){
            if(handrank-TPrank >=3) return false;
            else if(handrank-TPrank == 2){                  //two steps ahead
                if(handrank == 7){
                    if(TPfile != handfile) return false;    //not on same column
                    else{
                        Paths.add(Chess.createPiece(null,TargetPosition.pieceFile,TPrank+1));
                        Paths.add(Chess.createPiece(null,TargetPosition.pieceFile,TPrank));
                        if(!IfExistBlocks(Paths)){
                            Enpassantnow = 1;
                            Chess.EnpassantSet(onhand.pieceType,TargetPosition.pieceFile,TPrank);
                            return true;
                        }
                        else return false;
                    }
                }
                else return false;                          //not capable of two steps ahead
            }
            else if(handrank-TPrank == 1){                                           //one step
                if(TPfile-handfile >= 2) return false;
                else if(Math.abs(TPfile-handfile) == 1){
                    if(Chess.GetPiece(TargetPosition) != null){               //normal beat // check if this place can be lightened, as getpiece already added in ISPOT, but this place requires a test to check if activate Enpassant
                        if(IfSelfPieceOnTarget(onhand,TargetPosition)) return false;
                        else return true;
                    }
                    else if(Enpassantnow == 2){                              //En passant   //requires further test after moving section is finished
                        ReturnPiece temp = Chess.createPiece(Chess.Enpassant.pieceType,Chess.Enpassant.pieceFile,Chess.Enpassant.pieceRank-1);
                        TargetPosition.pieceType = Chess.Enpassant.pieceType;
                        if(temp.equals(TargetPosition)){
                            Enpassantnow = 3;
                            return true;
                        }
                        else return false;
                    }
                    else return false;
                }
                else{
                    Paths.add(Chess.createPiece(null,TargetPosition.pieceFile,TPrank));
                    if(IfExistBlocks(Paths)) return false;
                    else return true;
                }
            }
            else return false;
        }
        else if(onhand.equals("WR",true) || onhand.equals("BR",true)){
            if(!IfOnSameGridLine(handfile,TPfile,handrank,TPrank)) return false;
            if(Paths.size() != 0){
                if(IfExistBlocks(Paths)) return false;
            }
            if(IfSelfPieceOnTarget(onhand,TargetPosition)) return false;
            if(onhand.equals("a",false) && handrank == 1) Chess.IfMove[0] = true;
            if(onhand.equals("h",false) && handrank == 1) Chess.IfMove[2] = true;
            if(onhand.equals("a",false) && handrank == 8) Chess.IfMove[3] = true;
            if(onhand.equals("h",false) && handrank == 8) Chess.IfMove[5] = true;
            return true;
        }
        else if(onhand.equals("WN",true) || onhand.equals("BN",true)){
            if(!IfInKnightRange(handfile,TPfile,handrank,TPrank)) return false;
            if(IfSelfPieceOnTarget(onhand,TargetPosition)) return false;
            return true;
        }
        else if(onhand.equals("WB",true) || onhand.equals("BB",true)){
            if(!IfOnSameCrossLine(handfile,TPfile,handrank,TPrank)) return false;
            if(Paths.size() != 0){
                if(IfExistBlocks(Paths)) return false;
            }
            if(IfSelfPieceOnTarget(onhand,TargetPosition)) return false;
            return true;
        }
        else if(onhand.equals("WQ",true) || onhand.equals("BQ",true)){
            if(!IfOnSameGridLine(handfile,TPfile,handrank,TPrank) && !IfOnSameCrossLine(handfile,TPfile,handrank,TPrank)) return false;
            if(Paths.size() != 0){
                if(IfExistBlocks(Paths)) return false;
            }
            if(IfSelfPieceOnTarget(onhand,TargetPosition)) return false;
            return true;
        }
        else if(onhand.equals("WK",true) || onhand.equals("BK",true)){
            if(handfile == TPfile && handrank == TPrank) return false;
            if(Math.abs(handfile-TPfile)<=1 && Math.abs(handrank-TPrank)<=1){
                if(IfSelfPieceOnTarget(onhand,TargetPosition)) return false;
                if(onhand.equals("WK",true)) Chess.IfMove[1] = true;
                else Chess.IfMove[4] = true;
                return true;
            }
            else if(!IfCastlePossible(onhand,TargetPosition)) return false;
            else{
                if(onhand.equals("WK",true)) Chess.IfMove[1] = true;
                else Chess.IfMove[4] = true;
                return true;
            }
        }
        return false;
    }
    
    public static boolean IfExistBlocks(ArrayList<ComparablePiece> paths){      //Simple checker to check if there are pieces blocked on the move paths
        for(int i=0;i<paths.size();i++){
            ReturnPiece temp = Chess.GetPiece(paths.get(i));
            if(temp!= null) return true;
        }
        return false;
    }
    
    public static boolean IfSelfPieceOnTarget(ComparablePiece onhand, ComparablePiece TargetPosition){  //Simple checker to check if piece on target is actually ally
        if(Chess.GetPiece(TargetPosition) == null) return false;
        boolean IW1 = Chess.IfWhite(onhand);
        boolean IW2 = Chess.IfWhite(TargetPosition);
        if((IW1 && IW2)||(!IW1 && !IW2)){
            return true;
        }
        return false;
    }
    
    public static boolean IfOnSameGridLine(int handfile, int TPfile, int handrank, int TPrank){ //Check if the onhand piece position and the target position is on same grid line
        if(((handfile == TPfile) && (handrank == TPrank)) || ((handfile != TPfile) && (handrank != TPrank))) return false;
        else{
            if(handfile == TPfile){
                for(int i=1;i<Math.abs(TPrank-handrank);i++){
                    if(TPrank > handrank) Paths.add(Chess.createPiece(null,InttoPF(TPfile),TPrank-i));
                    else Paths.add(Chess.createPiece(null,InttoPF(TPfile),TPrank+i));
                }
            }
            else{
                for(int i=1;i<Math.abs(TPfile-handfile);i++){
                    if(TPfile > handfile) Paths.add(Chess.createPiece(null,InttoPF(TPfile-i),TPrank));
                    else Paths.add(Chess.createPiece(null,InttoPF(TPfile+i),TPrank));
                }
            }
        }
        return true;
    }
    
    public static boolean IfInKnightRange(int handfile, int TPfile, int handrank, int TPrank){  //check if target position is moveable by onhand piece knight
        if(((Math.abs(handfile-TPfile) == 2) && (Math.abs(handrank-TPrank) == 1)) || ((Math.abs(handfile-TPfile) == 1) && (Math.abs(handrank-TPrank) == 2))) return true;
        else return false;
    }
    
    public static boolean IfOnSameCrossLine(int handfile, int TPfile, int handrank, int TPrank){    //check if target position is on same croos line with onhand piece position
        if((handfile == TPfile) && (handrank == TPrank)) return false;
        if(Math.abs(handfile-TPfile) != Math.abs(handrank-TPrank)) return false;
        else{
            for(int i=1;i<Math.abs(handfile-TPfile);i++){
                if(TPfile > handfile && TPrank > handrank){
                    Paths.add(Chess.createPiece(null,InttoPF(handfile+i),handrank+i));
                }
                else if(TPfile > handfile && TPrank < handrank){
                    Paths.add(Chess.createPiece(null,InttoPF(handfile+i),handrank-i));
                }
                else if(TPfile < handfile && TPrank > handrank){
                    Paths.add(Chess.createPiece(null,InttoPF(handfile-i),handrank+i));
                }
                else{
                    Paths.add(Chess.createPiece(null,InttoPF(handfile-i),handrank-i));
                }
            }
        }
        return true;
    }
    
    public static boolean IfCastlePossible(ComparablePiece onhand, ComparablePiece TargetPosition){
            //1.Check if right place for castle, if rook on the right place, and if both king and rook haven't been moved in this game;
            //2.Check if during check or under attack on king's path;
            //3.Check if path is clear without been blocked using IfExistBlocks;
        if(onhand.equals("WK",true)){       // white king
            if(!(TargetPosition.pieceRank == 1 && (TargetPosition.equals("c",false) || TargetPosition.equals("g",false)))) return false;
            else{
                if(TargetPosition.equals("c",false)){   // queen side
                    ReturnPiece temp = Chess.GetPiece(Chess.createPiece(null,InttoPF(1),1));
                    if(temp != null && temp.pieceType == ReturnPiece.PieceType.WR){
                        if(Chess.IfMove[0] == false && Chess.IfMove[1] == false){
                            if(Chess.IfCheck[0] == true) return false;
                            else if(CheckChecker.IfCauseCheck(onhand, Chess.createPiece(null,InttoPF(4),1),Chess.player)) return false;
                            else if(CheckChecker.IfCauseCheck(onhand, Chess.createPiece(null,InttoPF(3),1),Chess.player)) return false;
                            else{
                                Paths.add(Chess.createPiece(null,InttoPF(2),1));
                                Paths.add(Chess.createPiece(null,InttoPF(3),1));
                                Paths.add(Chess.createPiece(null,InttoPF(4),1));
                                if(IfExistBlocks(Paths)) return false;
                                else{
                                    Chess.CastleCall = true;
                                    Chess.IfMove[0] = true;
                                    Chess.IfMove[1] = true;
                                    return true;
                                }
                            }
                        }
                        else return false;
                    }
                    else return false;
                }
                else{       //king side
                    ReturnPiece temp = Chess.GetPiece(Chess.createPiece(null,InttoPF(8),1));
                    if(temp != null && temp.pieceType == ReturnPiece.PieceType.WR){
                        if(Chess.IfMove[1] == false && Chess.IfMove[2] == false){
                            if(Chess.IfCheck[0] == true) return false;
                            else if(CheckChecker.IfCauseCheck(onhand, Chess.createPiece(null,InttoPF(6),1),Chess.player)) return false;
                            else if(CheckChecker.IfCauseCheck(onhand, Chess.createPiece(null,InttoPF(7),1),Chess.player)) return false;
                            else{
                                Paths.add(Chess.createPiece(null,InttoPF(6),1));
                                Paths.add(Chess.createPiece(null,InttoPF(7),1));
                                if(IfExistBlocks(Paths)) return false;
                                else{
                                    Chess.CastleCall = true;
                                    Chess.IfMove[1] = true;
                                    Chess.IfMove[2] = true;
                                    return true;
                                }
                            }
                        }
                        else return false;
                    }
                    else return false;
                }

            }
        }
        else{   //black king
            if(!(TargetPosition.pieceRank == 8 && (TargetPosition.equals("c",false) || TargetPosition.equals("g",false)))) return false;
            else{
                if(TargetPosition.equals("c",false)){   // queen side
                    ReturnPiece temp = Chess.GetPiece(Chess.createPiece(null,InttoPF(1),8));
                    if(temp != null && temp.pieceType == ReturnPiece.PieceType.BR){
                        if(Chess.IfMove[3] == false && Chess.IfMove[4] == false){
                            if(Chess.IfCheck[1] == true) return false;
                            else if(CheckChecker.IfCauseCheck(onhand, Chess.createPiece(null,InttoPF(4),8),Chess.player)) return false;
                            else if(CheckChecker.IfCauseCheck(onhand, Chess.createPiece(null,InttoPF(3),8),Chess.player)) return false;
                            else{
                                Paths.add(Chess.createPiece(null,InttoPF(2),8));
                                Paths.add(Chess.createPiece(null,InttoPF(3),8));
                                Paths.add(Chess.createPiece(null,InttoPF(4),8));
                                if(IfExistBlocks(Paths)) return false;
                                else{
                                    Chess.CastleCall = true;
                                    Chess.IfMove[3] = true;
                                    Chess.IfMove[4] = true;
                                    return true;
                                }
                            }
                        }
                        else return false;
                    }
                    else return false;
                }
                else{       //king side
                    ReturnPiece temp = Chess.GetPiece(Chess.createPiece(null,InttoPF(8),8));
                    if(temp != null && temp.pieceType == ReturnPiece.PieceType.BR){
                        if(Chess.IfMove[4] == false && Chess.IfMove[5] == false){
                            if(Chess.IfCheck[1] == true) return false;
                            else if(CheckChecker.IfCauseCheck(onhand, Chess.createPiece(null,InttoPF(6),8),Chess.player)) return false;
                            else if(CheckChecker.IfCauseCheck(onhand, Chess.createPiece(null,InttoPF(7),8),Chess.player)) return false;
                            else{
                                Paths.add(Chess.createPiece(null,InttoPF(6),8));
                                Paths.add(Chess.createPiece(null,InttoPF(7),8));
                                if(IfExistBlocks(Paths)) return false;
                                else{
                                    Chess.CastleCall = true;
                                    Chess.IfMove[4] = true;
                                    Chess.IfMove[5] = true;
                                    return true;
                                }
                            }
                        }
                        else return false;
                    }
                    else return false;
                }
            }
        }
    }
    
    public static int PieceFileTranslator(ComparablePiece pf){
        if(pf.equals("a",false)) return 1;
        if(pf.equals("b",false)) return 2;
        if(pf.equals("c",false)) return 3;
        if(pf.equals("d",false)) return 4;
        if(pf.equals("e",false)) return 5;
        if(pf.equals("f",false)) return 6;
        if(pf.equals("g",false)) return 7;
        if(pf.equals("h",false)) return 8;
        return -1;
    }
    
    public static ReturnPiece.PieceFile InttoPF(int i){
        if(i == 1) return ReturnPiece.PieceFile.a;
        if(i == 2) return ReturnPiece.PieceFile.b;
        if(i == 3) return ReturnPiece.PieceFile.c;
        if(i == 4) return ReturnPiece.PieceFile.d;
        if(i == 5) return ReturnPiece.PieceFile.e;
        if(i == 6) return ReturnPiece.PieceFile.f;
        if(i == 7) return ReturnPiece.PieceFile.g;
        if(i == 8) return ReturnPiece.PieceFile.h;
        return null;
    }
    
    public static ReturnPiece.PieceType StringtoPT(String s){
        return ReturnPiece.PieceType.valueOf(s);
    }
    
    public static ReturnPiece.PieceFile StringtoPF(String s){
        return ReturnPiece.PieceFile.valueOf(s);
    }
    
}
