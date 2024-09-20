package chess;

import java.util.ArrayList;

public class CheckmateChecker extends CheckChecker{
    static ArrayList<ReturnPiece[]> PossibleMoves;
    
    public static boolean IfCheckmate(Chess.Player side){
        PossibleMoves = new ArrayList<ReturnPiece[]>();
        VirRP.piecesOnBoard = new ArrayList<ReturnPiece>();
        for(int i=0;i<Chess.RP.piecesOnBoard.size();i++) VirRP.piecesOnBoard.add(NewRP(Chess.RP.piecesOnBoard.get(i)));
        for(int i=0;i<VirRP.piecesOnBoard.size();i++){
            ReturnPiece temp = VirRP.piecesOnBoard.get(i);
            if(temp.pieceType == ReturnPiece.PieceType.WP && side == Chess.Player.white) FindPawnMoves(temp, 0);
            if(temp.pieceType == ReturnPiece.PieceType.BP && side == Chess.Player.black) FindPawnMoves(temp, 1);
            if(temp.pieceType == ReturnPiece.PieceType.WR && side == Chess.Player.white) FindRookMoves(temp);
            if(temp.pieceType == ReturnPiece.PieceType.BR && side == Chess.Player.black) FindRookMoves(temp);
            if(temp.pieceType == ReturnPiece.PieceType.WN && side == Chess.Player.white) FindKnightMoves(temp);
            if(temp.pieceType == ReturnPiece.PieceType.BN && side == Chess.Player.black) FindKnightMoves(temp);
            if(temp.pieceType == ReturnPiece.PieceType.WB && side == Chess.Player.white) FindBishopMoves(temp);
            if(temp.pieceType == ReturnPiece.PieceType.BB && side == Chess.Player.black) FindBishopMoves(temp);
            if(temp.pieceType == ReturnPiece.PieceType.WQ && side == Chess.Player.white) FindQueenMoves(temp);
            if(temp.pieceType == ReturnPiece.PieceType.BQ && side == Chess.Player.black) FindQueenMoves(temp);
            if(temp.pieceType == ReturnPiece.PieceType.WK && side == Chess.Player.white) FindKingMoves(temp);
            if(temp.pieceType == ReturnPiece.PieceType.BK && side == Chess.Player.black) FindKingMoves(temp);
        }
        CMCOn = true;
        for(int j=0;j<PossibleMoves.size();j++){
            ReturnPiece[] temparr = PossibleMoves.get(j);
            //System.out.println(temparr[0].pieceFile+" "+temparr[0].pieceRank+"  "+temparr[1].pieceFile+" "+temparr[1].pieceRank);
            ReturnPiece p1 = temparr[0];
            ReturnPiece p2 = temparr[1];
            if(temparr.length == 3) VirEnpassantnow = true;
            ComparablePiece p3 = Chess.createPiece(p1.pieceType,p1.pieceFile,p1.pieceRank);
            ComparablePiece p4 = Chess.createPiece(p2.pieceType,p2.pieceFile,p2.pieceRank);
            if(!IfCauseCheck(p3,p4,side)){
                CMCOn = false;
                if(VirEnpassantnow) VirEnpassantnow = false;
                //System.out.println(p3.pieceFile+" "+p3.pieceRank+"  "+p4.pieceFile+" "+p4.pieceRank);
                return false;
            }
        }
        CMCOn = false;
        return true;
    }
    
    public static void FindPawnMoves(ReturnPiece onhand, int type){     //0 to WP, 1 to BP
        if(type == 0){
            ReturnPiece temp = Chess.createPiece(null,onhand.pieceFile,onhand.pieceRank+2);
            if(onhand.pieceRank == 2 && VirtualGetPiece(temp) == null){
                ReturnPiece[] temparr = {onhand, Chess.createPiece(temp.pieceType,temp.pieceFile,temp.pieceRank)};
                PossibleMoves.add(temparr);
            }
            temp.pieceRank = temp.pieceRank-1;
            if(temp.pieceRank <= 8){    //dealing for bug extreme cases, normally this is unnecessary, but in case
                if(VirtualGetPiece(temp) == null){
                    ReturnPiece[] temparr = {onhand, Chess.createPiece(temp.pieceType,temp.pieceFile,temp.pieceRank)};
                    PossibleMoves.add(temparr);
                }
                temp.pieceFile = PieceFileModifier(onhand, -1);
                if(temp.pieceFile != null && VirtualGetPiece(temp) != null && !(Chess.IfWhite(temp))){
                    ReturnPiece[] temparr = {onhand, Chess.createPiece(temp.pieceType,temp.pieceFile,temp.pieceRank)};
                    PossibleMoves.add(temparr);
                }
                if(temp.pieceFile != null && Chess.Enpassant.pieceFile == temp.pieceFile && Chess.Enpassant.pieceRank + 1 == temp.pieceRank){
                    ReturnPiece[] temparr = {onhand, Chess.createPiece(temp.pieceType,temp.pieceFile,temp.pieceRank), Chess.Enpassant};
                    PossibleMoves.add(temparr);
                }
                temp.pieceFile = PieceFileModifier(onhand,1);
                if(temp.pieceFile != null && VirtualGetPiece(temp) != null && !(Chess.IfWhite(temp))){
                    ReturnPiece[] temparr = {onhand, Chess.createPiece(temp.pieceType,temp.pieceFile,temp.pieceRank)};
                    PossibleMoves.add(temparr);
                }
                if(temp.pieceFile != null && Chess.Enpassant.pieceFile == temp.pieceFile && Chess.Enpassant.pieceRank + 1 == temp.pieceRank){
                    ReturnPiece[] temparr = {onhand, Chess.createPiece(temp.pieceType,temp.pieceFile,temp.pieceRank), Chess.Enpassant};
                    PossibleMoves.add(temparr);
                }
            }
        }
        else{
            ReturnPiece temp = Chess.createPiece(null,onhand.pieceFile,onhand.pieceRank-2);
            if(onhand.pieceRank == 7 && VirtualGetPiece(temp) == null){
                ReturnPiece[] temparr = {onhand, Chess.createPiece(temp.pieceType,temp.pieceFile,temp.pieceRank)};
                PossibleMoves.add(temparr);
            }
            temp.pieceRank = temp.pieceRank+1;
            if(temp.pieceRank >= 1){    // as above
                if(VirtualGetPiece(temp) == null){
                    ReturnPiece[] temparr = {onhand, Chess.createPiece(temp.pieceType,temp.pieceFile,temp.pieceRank)};
                    PossibleMoves.add(temparr);
                }
                temp.pieceFile = PieceFileModifier(onhand, -1);
                if(temp.pieceFile != null && VirtualGetPiece(temp) != null && Chess.IfWhite(temp)){
                    ReturnPiece[] temparr = {onhand, Chess.createPiece(temp.pieceType,temp.pieceFile,temp.pieceRank)};
                    PossibleMoves.add(temparr);
                }
                if(temp.pieceFile != null && Chess.Enpassant.pieceFile == temp.pieceFile && Chess.Enpassant.pieceRank - 1 == temp.pieceRank){
                    ReturnPiece[] temparr = {onhand, Chess.createPiece(temp.pieceType,temp.pieceFile,temp.pieceRank), Chess.Enpassant};
                    PossibleMoves.add(temparr);
                }
                temp.pieceFile = PieceFileModifier(onhand,1);
                if(temp.pieceFile != null && VirtualGetPiece(temp) != null && Chess.IfWhite(temp)){
                    ReturnPiece[] temparr = {onhand, Chess.createPiece(temp.pieceType,temp.pieceFile,temp.pieceRank)};
                    PossibleMoves.add(temparr);
                }
                if(temp.pieceFile != null && Chess.Enpassant.pieceFile == temp.pieceFile && Chess.Enpassant.pieceRank - 1 == temp.pieceRank){
                    ReturnPiece[] temparr = {onhand, Chess.createPiece(temp.pieceType,temp.pieceFile,temp.pieceRank), Chess.Enpassant};
                    PossibleMoves.add(temparr);
                }
            }
        }
    }
    
    public static void FindKnightMoves(ReturnPiece onhand){     //0 to WN, 1 to BN
        for(int i=1;i<=2;i++){
            CheckIfPossible(onhand,i,3-i);
            CheckIfPossible(onhand,i,-3+i);
            CheckIfPossible(onhand,-i,3-i);
            CheckIfPossible(onhand,-i,-3+i);
        }
    }
    
    public static void FindBishopMoves(ReturnPiece onhand){     //0 to WB, 1 to BB
        CheckByLine(onhand,1,1);
        CheckByLine(onhand,1,-1);
        CheckByLine(onhand,-1,-1);
        CheckByLine(onhand,-1,1);
    }
    
    public static void FindRookMoves(ReturnPiece onhand){     //0 to WR, 1 to BR
        CheckByLine(onhand,0,1);
        CheckByLine(onhand,1,0);
        CheckByLine(onhand,0,-1);
        CheckByLine(onhand,-1,0);
    }
    
    public static void FindQueenMoves(ReturnPiece onhand){     //0 to WQ, 1 to BQ
        FindBishopMoves(onhand);
        FindRookMoves(onhand);
    }
    
    public static void FindKingMoves(ReturnPiece onhand){     //0 to WK, 1 to BK
        CheckIfPossible(onhand,0,1);
        CheckIfPossible(onhand,1,1);
        CheckIfPossible(onhand,1,0);
        CheckIfPossible(onhand,1,-1);
        CheckIfPossible(onhand,0,-1);
        CheckIfPossible(onhand,-1,-1);
        CheckIfPossible(onhand,-1,0);
        CheckIfPossible(onhand,-1,1);
    }
    
    public static void CheckIfPossible(ReturnPiece onhand, int hor, int ver){
        ReturnPiece temp = Chess.createPiece(onhand.pieceType,PieceFileModifier(onhand,hor),onhand.pieceRank+ver);
        if(temp.pieceFile == null) return;
        if(temp.pieceRank > 8 || temp.pieceRank < 1) return;
        ReturnPiece TargetPosition = VirtualGetPiece(temp);
        if(TargetPosition != null &&((Chess.IfWhite(onhand) && Chess.IfWhite(TargetPosition))||(!Chess.IfWhite(onhand) && !Chess.IfWhite(TargetPosition)))) return;
        ReturnPiece[] temparr = {Chess.createPiece(onhand.pieceType,onhand.pieceFile,onhand.pieceRank),temp};
        PossibleMoves.add(temparr);
    }
    
    public static void CheckByLine(ReturnPiece onhand, int hor, int ver){
        ReturnPiece temp = Chess.createPiece(null,onhand.pieceFile,onhand.pieceRank);
        while(true){
            temp.pieceFile = PieceFileModifier(temp,hor);
            temp.pieceRank = temp.pieceRank + ver;
            if(temp.pieceFile == null || temp.pieceRank > 8 || temp.pieceRank < 1) return;
            ReturnPiece TargetPosition = VirtualGetPiece(temp);
            if(TargetPosition != null){
                if((Chess.IfWhite(onhand) && Chess.IfWhite(TargetPosition))||(!Chess.IfWhite(onhand) && !Chess.IfWhite(TargetPosition))) return;
                else{
                    ReturnPiece[] temparr = {Chess.createPiece(onhand.pieceType,onhand.pieceFile,onhand.pieceRank),Chess.createPiece(null,temp.pieceFile,temp.pieceRank)};
                    PossibleMoves.add(temparr);
                    return;
                }
            }
            else{
                ReturnPiece[] temparr = {Chess.createPiece(onhand.pieceType,onhand.pieceFile,onhand.pieceRank),Chess.createPiece(null,temp.pieceFile,temp.pieceRank)};
                PossibleMoves.add(temparr);
            }
        }
    }
}
