package com.chess.engine.board;

import com.chess.engine.Alliance;
import com.chess.engine.chess_pieces.*;
import com.google.common.collect.ImmutableList;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

public class ChessBoard {

    private final List<Tile> gameBoard;
    private final Collection<Piece> whitePieces;
    private final Collection<Piece> blackPieces;

    public ChessBoard(Builder builder) {
        this.gameBoard = createGameBoard(builder);
        this.whitePieces = calculateActivePieces(this.gameBoard, Alliance.WHITE);
        this.blackPieces = calculateActivePieces(this.gameBoard, Alliance.BLACK);
    }

    private Collection<Piece> calculateActivePieces(final List<Tile> gameBoard,
                                                    final Alliance alliance) {
        final List<Piece> activePieces = new ArrayList<>();
        for(final Tile tile: gameBoard) {
            if (tile.isTileOccupied()) {
                final Piece piece = tile.getPiece();
                if(piece.getPieceAlliance() == alliance){
                    activePieces.add(piece);
                }
            }
        }
        return ImmutableList.copyOf(activePieces);
    }

    private static List<Tile> createGameBoard(final Builder builder) {

        final Tile[] tiles = IntStream.range(0, BoardUtils.NUM_TILES)
                .mapToObj(i -> Tile.createTile(i, builder.boardConfig.get(i))).toArray(Tile[]::new);

        return ImmutableList.copyOf(tiles);
    }

    public static ChessBoard createInitialBoard(){
        final Builder builder = new Builder();
        // BLACK Layout
        builder.setPiece(new Rook(0, Alliance.BLACK));
        builder.setPiece(new Knight(1, Alliance.BLACK));
        builder.setPiece(new Bishop(2, Alliance.BLACK));
        builder.setPiece(new Queen(3, Alliance.BLACK));
        builder.setPiece(new King(4, Alliance.BLACK));
        builder.setPiece(new Bishop(5, Alliance.BLACK));
        builder.setPiece(new Knight(6, Alliance.BLACK));
        builder.setPiece(new Rook(7, Alliance.BLACK));
        generatePawns(builder, Alliance.BLACK, 8, 15);
        // WHITE Layout
        generatePawns(builder, Alliance.WHITE, 48, 55);
        builder.setPiece(new Rook(56, Alliance.WHITE));
        builder.setPiece(new Knight(57, Alliance.WHITE));
        builder.setPiece(new Bishop(58, Alliance.WHITE));
        builder.setPiece(new Queen(59, Alliance.WHITE));
        builder.setPiece(new King(60, Alliance.WHITE));
        builder.setPiece(new Bishop(61, Alliance.WHITE));
        builder.setPiece(new Knight(62, Alliance.WHITE));
        builder.setPiece(new Rook(63, Alliance.WHITE));
        // WHITE starts the game
        builder.setMoveMaker(Alliance.WHITE);

        return builder.build();
    }

    // TODO: check out this method
    private static void generatePawns(final Builder builder, final Alliance alliance, int firstPosition, int lastPosition) {
        IntStream.rangeClosed(firstPosition, lastPosition).mapToObj(i -> new Pawn(i, alliance))
                                                          .forEach(builder::setPiece);
    }

    public Tile getTile(final int tileCoordinate) {
        return gameBoard.get(tileCoordinate);
    }

    public static class Builder {

        Map<Integer, Piece> boardConfig;
        Alliance nextMoveMaker;

        public Builder() {
        }

        public Builder setPiece(final Piece piece) {
            this.boardConfig.put(piece.getPiecePosition(), piece);
            return this;
        }
        public Builder setMoveMaker(final Alliance alliance) {
            this.nextMoveMaker = nextMoveMaker;
            return this;
        }

        public ChessBoard build() {
            return new ChessBoard(this);
        }

    }


}
