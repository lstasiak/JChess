package com.chess.engine.board;

import com.chess.engine.board.ChessBoard.Builder;
import com.chess.engine.chess_pieces.Pawn;
import com.chess.engine.chess_pieces.Piece;

import java.util.Objects;

public abstract class Move {
    final ChessBoard board; // incoming board ref
    private final Piece movedPiece;
    private final int destinationCoordinate;
    public static final Move NULL_MOVE = new NullMove();

    private Move(final ChessBoard board,
                final Piece movedPiece,
                final int destinationCoordinate) {
        this.board = board;
        this.movedPiece = movedPiece;
        this.destinationCoordinate = destinationCoordinate;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final Move move = (Move) o;
        return getDestinationCoordinate() == move.getDestinationCoordinate() &&
                getMovedPiece().equals(move.getMovedPiece());
    }

    @Override
    public int hashCode() {
        return Objects.hash(movedPiece, destinationCoordinate);
    }

    public int getDestinationCoordinate() {
        return this.destinationCoordinate;
    }
    public Piece getMovedPiece() {
        return this.movedPiece;
    }
    private int getCurrentCoordinate() {
        return this.getMovedPiece().getPiecePosition();
    }

    public boolean isAttack() {
        return false;
    }
    public boolean isCastlingMove() {
        return false;
    }
    public Piece getAttackedPiece() {
        return null;
    }

    // return a new board
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


    // Subclasses

    public static final class MajorMove extends Move {

        public MajorMove(final ChessBoard board,
                         final Piece movedPiece,
                         final int destinationCoordinate) {
            super(board, movedPiece, destinationCoordinate);
        }
    }
    public static class AttackMove extends Move {
        private final Piece attackedPiece;

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

        @Override
        public Piece getAttackedPiece() {
            return attackedPiece;
        }

        @Override
        public boolean isAttack() {
            return true;
        }

        @Override
        public int hashCode() {
            return this.attackedPiece.hashCode() + super.hashCode();
        }
        // TODO check for any errors
        @Override
        public boolean equals(Object o) {
            if(this == o) return true;
            if (!(o instanceof AttackMove) ) return false;
            final AttackMove otherAttackMove = (AttackMove) o;
            return super.equals(otherAttackMove) && getAttackedPiece().equals(otherAttackMove.getAttackedPiece());
        }
    }

    public static final class PawnMove extends Move {

        public PawnMove(final ChessBoard board,
                        final Piece movedPiece,
                        final int destinationCoordinate) {
            super(board, movedPiece, destinationCoordinate);
        }
    }
    public static class PawnAttackMove extends AttackMove {

        public PawnAttackMove(final ChessBoard board,
                         final Piece movedPiece,
                         final int destinationCoordinate,
                         final Piece attackedPiece) {
            super(board, movedPiece, destinationCoordinate, attackedPiece);
        }
    }

    public static final class PawnEnPassantMove extends PawnAttackMove {

        public PawnEnPassantMove(final ChessBoard board,
                                 final Piece movedPiece,
                                 final int destinationCoordinate,
                                 final Piece attackedPiece) {
            super(board, movedPiece, destinationCoordinate, attackedPiece);
        }
    }

    public static final class PawnJump extends Move {

        public PawnJump(final ChessBoard board,
                        final Piece movedPiece,
                        final int destinationCoordinate) {
            super(board, movedPiece, destinationCoordinate);
        }

        @Override
        public ChessBoard execute() {
            final Builder builder = new Builder();

            this.board.currentPlayer().getActivePieces().stream()
                    .filter(piece -> !this.getMovedPiece().equals(piece))
                    .forEach(builder::setPiece);

            this.board.currentPlayer().getOpponent().getActivePieces()
                    .forEach(builder::setPiece);

            final Pawn movedPawn = (Pawn) this.getMovedPiece().movePiece(this);
            builder.setPiece(movedPawn);
            builder.setEnPassantPawn(movedPawn);
            builder.setMoveMaker(this.board.currentPlayer().getOpponent().getAlliance());

            return builder.build();
        }
    }

    static abstract class CastleMove extends Move {

        public CastleMove(final ChessBoard board,
                          final Piece movedPiece,
                          final int destinationCoordinate) {
            super(board, movedPiece, destinationCoordinate);
        }
    }
    public static final class KingSideCastleMove extends CastleMove {

        public KingSideCastleMove(final ChessBoard board,
                                  final Piece movedPiece,
                                  final int destinationCoordinate) {
            super(board, movedPiece, destinationCoordinate);
        }
    }

    public static final class QueenSideCastleMove extends CastleMove {

        public QueenSideCastleMove(final ChessBoard board,
                                    final Piece movedPiece,
                                    final int destinationCoordinate) {
            super(board, movedPiece, destinationCoordinate);
        }
    }

    public static final class NullMove extends CastleMove {

        public NullMove() {
            super(null, null, -1);  // FOR NOW
        }
        @Override
        public ChessBoard execute() {
            throw new RuntimeException("cannot execute the null move!");
        }
    }
    public static class FactoryMove {

        public FactoryMove() {
            throw new RuntimeException("Not instantiable!");
        }
        public static Move createMove(final ChessBoard board,
                                      final int currentCoordinate,
                                      final int destinationCoordinate) {

            return board.getAllLegalMoves().stream()
                    .filter(move -> move.getCurrentCoordinate() == currentCoordinate)
                    .filter(move -> move.getDestinationCoordinate() == destinationCoordinate)
                    .findFirst()
                    .orElse(NULL_MOVE);
        }

    }

}
