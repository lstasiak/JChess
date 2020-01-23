package com.chess.engine.board;

import com.chess.engine.board.ChessBoard.Builder;
import com.chess.engine.chess_pieces.Piece;

public abstract class Move {
    final ChessBoard board; // incoming board ref
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
    public Piece getMovedPiece() {
        return this.movedPiece;
    }
    public abstract ChessBoard execute();

    public static final class MajorMove extends Move {

        public MajorMove(final ChessBoard board,
                         final Piece movedPiece,
                         final int destinationCoordinate) {
            super(board, movedPiece, destinationCoordinate);
        }
        // return a new board
        @Override
        public ChessBoard execute() {
            // generate a map with the board configuration for the current player
            final Builder builder = new Builder();
            // we set all pieces except the current moving piece
            this.board.currentPlayer().getActivePieces().stream()
                    .filter(piece -> !this.movedPiece.equals(piece)) // equal structure not ref!!!
                    .forEach(builder::setPiece);

            // doing the same thing for the opponent pieces, but in this case all pieces are set
            this.board.currentPlayer().getOpponent()
                    .getActivePieces().forEach(builder::setPiece);

            // move the piece via this setPiece
            builder.setPiece(null);
            // set the incoming move maker to the opponent
            builder.setMoveMaker(this.board.currentPlayer()
                    .getOpponent().getAlliance());

            return builder.build();
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
