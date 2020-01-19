package com.chess.engine.chess_pieces;

import com.chess.engine.Alliance;
import com.chess.engine.board.ChessBoard;
import com.chess.engine.board.Move;

import java.util.Collection;

public abstract class Piece {

    protected final int piecePosition;
    protected final Alliance pieceAlliance;

    Piece(final int piecePosition, final Alliance pieceAlliance) {
        this.piecePosition = piecePosition;
        this.pieceAlliance = pieceAlliance;
    }
    public Alliance getPieceAlliance() {
        return this.pieceAlliance;
    }
    public abstract Collection<Move> calculateLegalMoves(final ChessBoard chessBoard);

}
