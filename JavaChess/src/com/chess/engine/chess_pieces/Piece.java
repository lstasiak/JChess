package com.chess.engine.chess_pieces;

import com.chess.engine.Alliance;
import com.chess.engine.board.ChessBoard;
import com.chess.engine.board.Move;

import java.util.Collection;
import java.util.Objects;

public abstract class Piece {

    protected final int piecePosition;
    protected final Alliance pieceAlliance;
    protected final boolean isFirstMove;
    protected final PieceType pieceType;

    Piece(final PieceType pieceType,
          final int piecePosition,
          final Alliance pieceAlliance) {

        this.pieceType = pieceType;
        this.piecePosition = piecePosition;
        this.pieceAlliance = pieceAlliance;
        this.isFirstMove = false;
    }

    public int getPiecePosition(){
        return this.piecePosition;
    }
    public PieceType getPieceType() {
        return this.pieceType;
    }
    public boolean isFirstMove() {
        return this.isFirstMove;
    }
    public Alliance getPieceAlliance() {
        return this.pieceAlliance;
    }

    @Override
    public boolean equals(final Object other) {
        if (this == other) return true;
        if (other == null || getClass() != other.getClass()) return false;
        Piece piece = (Piece) other;
        return piecePosition == piece.piecePosition &&
                isFirstMove == piece.isFirstMove &&
                pieceAlliance == piece.pieceAlliance &&
                pieceType == piece.pieceType;
    }

    @Override
    public int hashCode() {
        return Objects.hash(piecePosition, pieceAlliance, isFirstMove, pieceType);
    }

    public abstract Collection<Move> calculateLegalMoves(final ChessBoard chessBoard);
    public abstract Piece movePiece(Move move);

    public enum PieceType {
        PAWN("P"){
            @Override
            public boolean isKing() {
                return false;
            }
        },
        KNIGHT("N") {
            @Override
            public boolean isKing() {
                return false;
            }
        },
        BISHOP("B") {
            @Override
            public boolean isKing() {
                return false;
            }
        },
        ROOK("R") {
            @Override
            public boolean isKing() {
                return false;
            }
        },
        QUEEN("Q"){
            @Override
            public boolean isKing() {
                return false;
            }
        },
        KING("K") {
            @Override
            public boolean isKing() {
                return true;
            }
        };

        private String pieceName;

        PieceType(final String pieceName) {
            this.pieceName = pieceName;
        }

        @Override
        public String toString() {
            return this.pieceName; // to test the app
        }

        public abstract boolean isKing();
    }

}
