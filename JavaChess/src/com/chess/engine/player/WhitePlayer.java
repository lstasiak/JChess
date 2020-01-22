package com.chess.engine.player;


import com.chess.engine.Alliance;
import com.chess.engine.board.ChessBoard;
import com.chess.engine.board.Move;
import com.chess.engine.chess_pieces.Piece;

import java.util.Collection;

public class WhitePlayer extends Player {

    public WhitePlayer(ChessBoard chessBoard,
                       Collection<Move> whiteStandardLegalMoves,
                       Collection<Move> blackStandardLegalMoves) {

        super(chessBoard, whiteStandardLegalMoves, blackStandardLegalMoves);
    }

    @Override
    protected Collection<Piece> getActivePieces() {
        return this.chessBoard.getWhitePieces();
    }

    @Override
    public Alliance getAlliance() {
        return Alliance.WHITE;
    }

    @Override
    public Player getOpponent() {
        return this.chessBoard.blackPlayer();
    }


}
