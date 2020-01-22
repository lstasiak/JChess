package com.chess.engine.board;

import com.chess.engine.chess_pieces.Piece;

public abstract class Move {
    final ChessBoard board;
    final Piece movedPiece;
    final int destinationCoordinate;

    private Move(final ChessBoard board,
                final Piece movedPiece,
                final int destinationCoordinate) {
        this.board = board;
        this.movedPiece = movedPiece;
        this.destinationCoordinate = destinationCoordinate;
    }

    public int getDestinationCoordinate() {
        return this.destinationCoordinate;
    }

    public abstract ChessBoard execute();


    public static final class MajorMove extends Move {

        public MajorMove(final ChessBoard board,
                         final Piece movedPiece,
                         final int destinationCoordinate) {
            super(board, movedPiece, destinationCoordinate);
        }

        @Override
        public ChessBoard execute() {
            return null;
        }
    }
    public static final class AttackMove extends Move {
        final Piece attackedPiece;

        public AttackMove(final ChessBoard board,
                          final Piece movedPiece,
                          final int destinationCoordinate,
                          final Piece attackedPiece) {
            super(board, movedPiece, destinationCoordinate);
            this.attackedPiece = attackedPiece;
        }

        @Override
        public ChessBoard execute() {
            return null;
        }
    }


}
