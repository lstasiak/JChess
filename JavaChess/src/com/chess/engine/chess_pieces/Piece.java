package com.chess.engine.chess_pieces;

import com.chess.engine.Alliance;
import com.chess.engine.board.ChessBoard;
import com.chess.engine.board.Move;

import java.util.Collection;

public abstract class Piece {

    protected final int piecePosition;
    protected final Alliance pieceAlliance;
    protected final boolean isFirstMove;

    Piece(final int piecePosition, final Alliance pieceAlliance) {
        this.piecePosition = piecePosition;
        this.pieceAlliance = pieceAlliance;
        this.isFirstMove = false;
    }

    public int getPiecePosition(){
        return this.piecePosition;
    }

    public boolean isFirstMove() {
        return this.isFirstMove;
    }
    public Alliance getPieceAlliance() {
        return this.pieceAlliance;
    }
    public abstract Collection<Move> calculateLegalMoves(final ChessBoard chessBoard);

}
