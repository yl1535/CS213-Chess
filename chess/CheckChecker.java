package chess;

import java.util.ArrayList;

public class CheckChecker {
    static ReturnPlay VirRP = new ReturnPlay();
    static boolean VirEnpassantnow = false;
    static boolean CMCOn = false;   //CheckmateChecker IfOn
    
    public static boolean IfCauseCheck(ComparablePiece onhand, ComparablePiece TargetPosition, Chess.Player side){
        VirRP.piecesOnBoard = new ArrayList<ReturnPiece>();
        for(int i=0;i<Chess.RP.piecesOnBoard.size();i++) VirRP.piecesOnBoard.add(NewRP(Chess.RP.piecesOnBoard.get(i)));
        if(VirtualGetPiece(TargetPosition) != null) VirtualRemovePiece(TargetPosition);
        if(Reachable.Enpassantnow == 3 || VirEnpassantnow){
            for(int i=0;i<VirRP.piecesOnBoard.size();i++){
                ReturnPiece temp = VirRP.piecesOnBoard.get(i);
                    if(temp.pieceRank == Chess.Enpassant.pieceRank && temp.pieceFile == Chess.Enpassant.pieceFile){
                        VirRP.piecesOnBoard.remove(i);
                    break;
                }
            }
        }
        VirtualSetPiece(onhand,TargetPosition);
        if(Chess.CastleCall){
            if(Chess.player == Chess.Player.white && TargetPosition.equals("c",false)){
                ReturnPiece Targetrook = VirtualGetPiece(Chess.createPiece(null,ReturnPiece.PieceFile.a,1));
                VirtualSetPiece(Targetrook,Chess.createPiece(null,ReturnPiece.PieceFile.d,1));
            }
            else if(Chess.player == Chess.Player.white && TargetPosition.equals("g",false)){
                ReturnPiece Targetrook = VirtualGetPiece(Chess.createPiece(null,ReturnPiece.PieceFile.h,1));
                VirtualSetPiece(Targetrook,Chess.createPiece(null,ReturnPiece.PieceFile.f,1));
            }
            else if(Chess.player == Chess.Player.black && TargetPosition.equals("c",false)){
                ReturnPiece Targetrook = VirtualGetPiece(Chess.createPiece(null,ReturnPiece.PieceFile.a,8));
                VirtualSetPiece(Targetrook,Chess.createPiece(null,ReturnPiece.PieceFile.d,8));
            }
            else if(Chess.player == Chess.Player.black && TargetPosition.equals("g",false)){
                ReturnPiece Targetrook = VirtualGetPiece(Chess.createPiece(null,ReturnPiece.PieceFile.h,8));
                VirtualSetPiece(Targetrook,Chess.createPiece(null,ReturnPiece.PieceFile.f,8));
            }
        }
        String tempExtra = "";
        if(!CMCOn && Chess.ExtraExist){
            if(!(Chess.Extra.equals("draw?"))){
                if(Chess.player == Chess.Player.white) tempExtra = "W"+Chess.Extra;
                else tempExtra = "B"+Chess.Extra;
                ReturnPiece temp = VirtualGetPiece(TargetPosition);
                temp.pieceType = Reachable.StringtoPT(tempExtra);
                VirtualSetPiece(temp);
            }
        }
        if(onhand.equals("WP",true) && TargetPosition.pieceRank == 8 && (CMCOn || !Chess.ExtraExist)){
            TargetPosition.pieceType = Reachable.StringtoPT("WQ");
            VirtualSetPiece(TargetPosition);
        }
        if(onhand.equals("BP",true) && TargetPosition.pieceRank == 1 && (CMCOn || !Chess.ExtraExist)){
            TargetPosition.pieceType = Reachable.StringtoPT("BQ");
            VirtualSetPiece(TargetPosition);
        }
            //Create a virtual Pieceboard for (If this move is done) future, then examine if been checkmated in this future
            //Also, if have spare time test if this part can be using same method/code with the activate move part in Chess.java
                //Note that as are operating different ArrayLists (and different variables added into ArrayLists, important!), this will be difficult
        if(PawnChecker(side)) return true;
        if(KnightChecker(side)) return true;
        if(LineChecker(1,0,side)) return true;
        if(LineChecker(0,1,side)) return true;
        if(LineChecker(-1,0,side)) return true;
        if(LineChecker(0,-1,side)) return true;
        if(LineChecker(1,1,side)) return true;
        if(LineChecker(1,-1,side)) return true;
        if(LineChecker(-1,1,side)) return true;
        if(LineChecker(-1,-1,side)) return true;
        return false;
    }
    
    public static boolean IfCauseCheck(Chess.Player side){
        VirRP.piecesOnBoard = new ArrayList<ReturnPiece>();
        for(int i=0;i<Chess.RP.piecesOnBoard.size();i++) VirRP.piecesOnBoard.add(NewRP(Chess.RP.piecesOnBoard.get(i)));
        if(PawnChecker(side)) return true;
        if(KnightChecker(side)) return true;
        if(LineChecker(1,0,side)) return true;
        if(LineChecker(0,1,side)) return true;
        if(LineChecker(-1,0,side)) return true;
        if(LineChecker(0,-1,side)) return true;
        if(LineChecker(1,1,side)) return true;
        if(LineChecker(1,-1,side)) return true;
        if(LineChecker(-1,1,side)) return true;
        if(LineChecker(-1,-1,side)) return true;
        return false;
    }
    
    public static ReturnPiece NewRP(ReturnPiece RP){
        ReturnPiece NewRP = new ReturnPiece();
        NewRP.pieceType = RP.pieceType;
        NewRP.pieceFile = RP.pieceFile;
        NewRP.pieceRank = RP.pieceRank;
        return NewRP;
    }
    
    public static ReturnPiece VirtualGetPiece(ReturnPiece TargetPosition){
        for(int i=0;i<VirRP.piecesOnBoard.size();i++){
            ReturnPiece temp = VirRP.piecesOnBoard.get(i);
            if(temp.pieceRank == TargetPosition.pieceRank && temp.pieceFile == TargetPosition.pieceFile){
                return temp;
            }
        }
        return null;
    }
    
    public static void VirtualRemovePiece(ReturnPiece TargetPosition){
        for(int i=0;i<VirRP.piecesOnBoard.size();i++){
            ReturnPiece temp = VirRP.piecesOnBoard.get(i);
            if(temp.pieceRank == TargetPosition.pieceRank && temp.pieceFile == TargetPosition.pieceFile){
                VirRP.piecesOnBoard.remove(i);
                break;
            }
        }
    }
    
    public static void VirtualSetPiece(ReturnPiece onhand, ReturnPiece TargetPosition){
        for(int i=0;i<VirRP.piecesOnBoard.size();i++){
            ReturnPiece temp = VirRP.piecesOnBoard.get(i);
            if(temp.pieceRank == onhand.pieceRank && temp.pieceFile == onhand.pieceFile){
                temp.pieceRank = TargetPosition.pieceRank;
                temp.pieceFile = TargetPosition.pieceFile;
                VirRP.piecesOnBoard.set(i,temp);
                return;
            }
        }
    }
    
    public static void VirtualSetPiece(ReturnPiece TargetPosition){
        for(int i=0;i<VirRP.piecesOnBoard.size();i++){
            ReturnPiece temp = VirRP.piecesOnBoard.get(i);
            if(temp.pieceRank == TargetPosition.pieceRank && temp.pieceFile == TargetPosition.pieceFile){
                temp.pieceType = TargetPosition.pieceType;
                VirRP.piecesOnBoard.set(i,temp);
                return;
            }
        }
    }
    
    public static ReturnPiece GetKing(int type){    //0: white king, 1:black king
        for(int i=0;i<VirRP.piecesOnBoard.size();i++){
            ReturnPiece temp = VirRP.piecesOnBoard.get(i);
            if(type == 0 && temp.pieceType == ReturnPiece.PieceType.WK) return temp;
            if(type == 1 && temp.pieceType == ReturnPiece.PieceType.BK) return temp;
        }
        return null;
    }
    
    public static ReturnPiece.PieceFile PieceFileModifier(ReturnPiece RP, int value){
        ComparablePiece CP = new ComparablePiece();
        CP.pieceFile = RP.pieceFile;
        int PF = Reachable.PieceFileTranslator(CP);
        PF += value;
        if(PF > 8 || PF < 1) return null;
        return Reachable.InttoPF(PF);
    }
    
    public static boolean PawnChecker(Chess.Player side){
        if(side == Chess.Player.white){
            ReturnPiece WhiteKing = GetKing(0);
            if(WhiteKing.pieceRank == 8) return false;
            if(WhiteKing.pieceFile != ReturnPiece.PieceFile.a){
                ReturnPiece temp = Chess.createPiece(null,PieceFileModifier(WhiteKing,-1),WhiteKing.pieceRank+1);
                if(VirtualGetPiece(temp) != null && VirtualGetPiece(temp).pieceType == ReturnPiece.PieceType.BP) return true;
            }
            if(WhiteKing.pieceFile != ReturnPiece.PieceFile.h){
                ReturnPiece temp = Chess.createPiece(null,PieceFileModifier(WhiteKing,1),WhiteKing.pieceRank+1);
                if(VirtualGetPiece(temp) != null && VirtualGetPiece(temp).pieceType == ReturnPiece.PieceType.BP) return true;
            }
        }
        else{
            ReturnPiece BlackKing = GetKing(1);
            if(BlackKing.pieceRank == 1) return false;
            if(BlackKing.pieceFile != ReturnPiece.PieceFile.a){
                ReturnPiece temp = Chess.createPiece(null,PieceFileModifier(BlackKing,-1),BlackKing.pieceRank-1);
                if(VirtualGetPiece(temp) != null && VirtualGetPiece(temp).pieceType == ReturnPiece.PieceType.WP) return true;
            }
            if(BlackKing.pieceFile != ReturnPiece.PieceFile.h){
                ReturnPiece temp = Chess.createPiece(null,PieceFileModifier(BlackKing,1),BlackKing.pieceRank-1);
                if(VirtualGetPiece(temp) != null && VirtualGetPiece(temp).pieceType == ReturnPiece.PieceType.WP) return true;
            }
        }
        return false;
    }
    
    public static boolean KnightChecker(Chess.Player side){
        ReturnPiece.PieceType pt = null;
        ReturnPiece King = new ReturnPiece();
        if(side == Chess.Player.white){
            pt = ReturnPiece.PieceType.BN;
            King = GetKing(0);
        }
        else{
            pt = ReturnPiece.PieceType.WN;
            King = GetKing(1);
        }
        for(int i=1;i<=2;i++){
            if(PieceFileModifier(King,i) != null && King.pieceRank+3-i < 9){
                ReturnPiece temp = Chess.createPiece(null,PieceFileModifier(King,i),King.pieceRank+3-i);
                if(VirtualGetPiece(temp) != null && VirtualGetPiece(temp).pieceType == pt) return true;
            }
            if(PieceFileModifier(King,i) != null && King.pieceRank-3+i > 0){
                ReturnPiece temp = Chess.createPiece(null,PieceFileModifier(King,i),King.pieceRank-3+i);
                if(VirtualGetPiece(temp) != null && VirtualGetPiece(temp).pieceType == pt) return true;
            }
            if(PieceFileModifier(King,-i) != null && King.pieceRank+3-i < 9){
                ReturnPiece temp = Chess.createPiece(null,PieceFileModifier(King,-i),King.pieceRank+3-i);
                if(VirtualGetPiece(temp) != null && VirtualGetPiece(temp).pieceType == pt) return true;
            }
            if(PieceFileModifier(King,-i) != null && King.pieceRank-3+i > 0){
                ReturnPiece temp = Chess.createPiece(null,PieceFileModifier(King,-i),King.pieceRank-3+i);
                if(VirtualGetPiece(temp) != null && VirtualGetPiece(temp).pieceType == pt) return true;
            }
        }
        return false;
    }
    
    public static boolean LineChecker(int hor, int ver, Chess.Player side){    //each time add hor and ver to go find a new block, if either not equal to 0 then cross mode, else grid mode
        ReturnPiece.PieceType[] pt = new ReturnPiece.PieceType[2];
        ReturnPiece King = new ReturnPiece();
        if(side == Chess.Player.white){
            pt[0] = ReturnPiece.PieceType.BQ;
            King = GetKing(0);
            if(hor != 0 && ver != 0) pt[1] = ReturnPiece.PieceType.BB;
            else pt[1] = ReturnPiece.PieceType.BR;
        }
        else{
            pt[0] = ReturnPiece.PieceType.WQ;
            King = GetKing(1);
            if(hor != 0 && ver != 0) pt[1] = ReturnPiece.PieceType.WB;
            else pt[1] = ReturnPiece.PieceType.WR;
        }
        ReturnPiece temp = new ReturnPiece();
        temp.pieceFile = King.pieceFile;
        temp.pieceRank = King.pieceRank;
        while(true){
            if(PieceFileModifier(temp,hor) == null) break;
            else temp.pieceFile = PieceFileModifier(temp,hor);
            temp.pieceRank = temp.pieceRank + ver;
            if(temp.pieceRank > 8 || temp.pieceRank < 1) break;
            if(VirtualGetPiece(temp) != null){
                if(VirtualGetPiece(temp).pieceType == pt[0] || VirtualGetPiece(temp).pieceType == pt[1]) return true;
                else break;
            }
        }
        return false;
    }

}
