package com.chess.engine.player;
import com.chess.engine.Alliance;
import com.chess.engine.board.ChessBoard;
import com.chess.engine.board.Move;
import com.chess.engine.chess_pieces.King;
import com.chess.engine.chess_pieces.Piece;
import com.google.common.collect.ImmutableList;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public abstract class Player {

    protected final ChessBoard chessBoard;
    protected final King playerKing;
    protected final Collection<Move> legalMoves;
    private final boolean isInCheck;

    public Player(final ChessBoard chessBoard,
                  final Collection<Move> legalMoves,
                  final Collection<Move> opponentMoves) {

        this.chessBoard = chessBoard;
        this.playerKing = establishKing();
        this.legalMoves = legalMoves;
        this.isInCheck = !Player.calculateAttacksOnTile(this.playerKing.getPiecePosition(), opponentMoves).isEmpty();
    }

    public King getPlayerKing(){
        return this.playerKing;
    }

    public Collection<Move> getLegalMoves(){
        return this.legalMoves;
    }
    public abstract Collection<Piece> getActivePieces();
    public abstract Alliance getAlliance();
    public abstract Player getOpponent();

    private static Collection<Move> calculateAttacksOnTile(int piecePosition, Collection<Move> moves) {
        final List<Move> attackMoves = moves.stream()
                .filter(move -> piecePosition == move.getDestinationCoordinate())
                .collect(Collectors.toList());

        return ImmutableList.copyOf(attackMoves);
    }

    private King establishKing() throws RuntimeException {
        for (final Piece piece: getActivePieces()) {
            if (piece.getPieceType().isKing())
                return (King) piece;
        }
        throw  new RuntimeException("The chess board you set up is invalid!");
    }

    public boolean isMoveLegal(final Move move) {
        return this.legalMoves.contains(move);
    }

    // CHECK AND CHECKMATE
    public boolean isInCheck() {
        return this.isInCheck;
    }

    public boolean isInCheckMate() {
        return this.isInCheck && !hasEscapeMoves();
    }

    // NOT CHECK BUT NO MOVE POSSIBILITY
    public boolean isInStaleMate() {
        return !this.isInCheck && !hasEscapeMoves();
    }

    protected boolean hasEscapeMoves() {
        return this.legalMoves.stream()
                .map(this::makeMove)
                .anyMatch(transition -> transition.getMoveStatus().isDone());
    }
    public boolean isCastled() {
        return false;
    }

    // board wrapping
    public MoveTransition makeMove(final Move move) {
        if (!isMoveLegal(move)) // the illegal move
            return new MoveTransition(this.chessBoard, move, MoveStatus.ILLEGAL_MOVE);

        final ChessBoard transitionBoard = move.execute();
        final Collection<Move> kingAttacks = Player.calculateAttacksOnTile(transitionBoard.currentPlayer()
                .getOpponent().getPlayerKing().getPiecePosition(), transitionBoard.currentPlayer().getLegalMoves());

        if (!kingAttacks.isEmpty())
            return new MoveTransition(this.chessBoard, move, MoveStatus.LEAVES_PLAYER_IN_CHECK);

        // return new transition board wrapped in a new move transition
        return new MoveTransition(transitionBoard, move, MoveStatus.DONE);
    }
}
