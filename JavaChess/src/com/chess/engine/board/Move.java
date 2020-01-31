package com.chess.engine.board;

import com.chess.engine.board.ChessBoard.Builder;
import com.chess.engine.chess_pieces.Pawn;
import com.chess.engine.chess_pieces.Piece;
import com.chess.engine.chess_pieces.Rook;

import java.util.Objects;

public abstract class Move {
    protected final ChessBoard board; // incoming board ref
    private final Piece movedPiece;
    private final int destinationCoordinate;
    public static final Move NULL_MOVE = new NullMove();
    protected final boolean isFirstMove;

    private Move(final ChessBoard board,
                 final Piece movedPiece,
                 final int destinationCoordinate) {
        this.board = board;
        this.movedPiece = movedPiece;
        this.destinationCoordinate = destinationCoordinate;
        this.isFirstMove = movedPiece.isFirstMove();
    }
    private Move(final ChessBoard board,
                 final int destinationCoordinate) {
        this.board = board;
        this.destinationCoordinate = destinationCoordinate;
        this.movedPiece = null;
        this.isFirstMove = false;
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
    public int getCurrentCoordinate() {
        return this.getMovedPiece().getPiecePosition();
    }
    public ChessBoard getBoard() {
        return this.board;
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
        builder.setPiece(this.movedPiece.movePiece(this));
        // set the incoming move maker to the opponent
        builder.setMoveMaker(this.board.currentPlayer()
                .getOpponent().getAlliance());
        builder.setMoveTransition(this);
        return builder.build();
    }
    public ChessBoard undo() {
        final ChessBoard.Builder builder = new Builder();
        this.board.getAllPieces().stream().forEach(builder::setPiece);
        builder.setMoveMaker(this.board.currentPlayer().getAlliance());
        return builder.build();
    }

    public enum MoveStatus {
        DONE {
            @Override
            public boolean isDone() {
                return true;
            }
        },
        ILLEGAL_MOVE {
            @Override
            public boolean isDone() {
                return false;
            }
        },
        LEAVES_PLAYER_IN_CHECK {
            @Override
            public boolean isDone() {
                return false;
            }
        };

        public abstract boolean isDone();
    }


    // Subclasses

    public static final class MajorMove extends Move {

        public MajorMove(final ChessBoard board,
                         final Piece movedPiece,
                         final int destinationCoordinate) {
            super(board, movedPiece, destinationCoordinate);
        }

        @Override
        public boolean equals(Object o) {
            return super.equals(o);
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

        protected final Rook castleRook;
        protected final int castleRookStart;
        protected final int castleRookDest;

        public CastleMove(final ChessBoard board,
                          final Piece movedPiece,
                          final int destinationCoordinate,
                          final Rook castleRook,
                          final int castleRookStart,
                          final int castleRookDest) {
            super(board, movedPiece, destinationCoordinate);
            this.castleRook = castleRook;
            this.castleRookStart = castleRookStart;
            this.castleRookDest = castleRookDest;
        }

        public Rook getCastleRook() {
            return castleRook;
        }
        @Override
        public boolean isCastlingMove() {
            return true;
        }

        @Override
        public ChessBoard execute() {
            final Builder builder = new Builder();
            this.board.currentPlayer().getActivePieces().stream()
                    .filter(piece -> !this.getMovedPiece().equals(piece))
                    .filter(piece -> !this.castleRook.equals(piece))
                    .forEach(builder::setPiece);

            builder.setPiece(this.getMovedPiece().movePiece(this));
            builder.setPiece(new Rook(this.castleRookDest, this.castleRook.getPieceAlliance()));
            builder.setMoveMaker(this.board.currentPlayer().getOpponent().getAlliance());
            return builder.build();
        }
    }

    public static class KingSideCastleMove extends CastleMove {

        public KingSideCastleMove(final ChessBoard board,
                                  final Piece movedPiece,
                                  final int destinationCoordinate,
                                  final Rook castleRook,
                                  final int castleRookStart,
                                  final int castleRookDest) {

            super(board, movedPiece, destinationCoordinate,
                    castleRook, castleRookStart, castleRookDest);
        }

        @Override
        public String toString() {
            return "O-O"; // PGM convention
        }
    }

    public static class QueenSideCastleMove extends CastleMove {

        public QueenSideCastleMove(final ChessBoard board,
                                   final Piece movedPiece,
                                   final int destinationCoordinate,
                                   final Rook castleRook,
                                   final int castleRookStart,
                                   final int castleRookDest) {

            super(board, movedPiece, destinationCoordinate,
                    castleRook, castleRookStart, castleRookDest);
        }

        @Override
        public String toString() {
            return "O-O-O";
        }
    }

    private static class NullMove extends Move {
        private NullMove() {
            super(null, -1);
        }

        @Override
        public int getCurrentCoordinate() {
            return -1;
        }

        @Override
        public int getDestinationCoordinate() {
            return -1;
        }

        @Override
        public ChessBoard execute() {
            throw new RuntimeException("cannot execute null move!");
        }

        @Override
        public String toString() {
            return "Null Move";
        }
    }
    public static class FactoryMove {
        private static final Move NULL_MOVE = new NullMove();

        public FactoryMove() {
            throw new RuntimeException("Not instantiable!");
        }
        public static Move getNullMove() {
            return NULL_MOVE;
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
