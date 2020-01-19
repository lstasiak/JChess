package com.chess.engine.board;

import com.chess.engine.chess_pieces.Piece;
import com.google.common.collect.ImmutableMap;

import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public abstract class Tile {

    protected final int tileCoordinate; // immutable field
    private static final Map<Integer, EmptyTile> EMPTY_TILES_CACHE = createAllPossibleEmptyTiles();

    private static Map<Integer,EmptyTile> createAllPossibleEmptyTiles() {
        final Map<Integer, EmptyTile> emptyTileMap = IntStream.range(0, BoardUtils.NUM_TILES)
                .boxed()
                .collect(Collectors.toMap(i -> i, EmptyTile::new, (a, b) -> b));

        return ImmutableMap.copyOf(emptyTileMap); // from guava library --> more effective than Collections.unmodifiableMap()
    }

    private Tile(final int tileCoordinate) {
        this.tileCoordinate = tileCoordinate;
    }

    public static Tile createTile(final int tileCoordinate, final Piece piece) {
        return piece != null ? new OccupiedTile(tileCoordinate, piece): EMPTY_TILES_CACHE.get(tileCoordinate);
    }

    public abstract boolean isTileOccupied();
    public abstract Piece getPiece();

    public static final class EmptyTile extends Tile{
        EmptyTile(final int coordinate) {
            super(coordinate);
        }
        @Override
        public boolean isTileOccupied() {
            return false;
        }

        @Override
        public Piece getPiece() {
            return null;
        }
    }
    public static final class OccupiedTile extends Tile {
        private final Piece pieceOnTile;
        private OccupiedTile(int tileCoordinate, final Piece pieceOnTile) {
            super(tileCoordinate);
            this.pieceOnTile = pieceOnTile;
        }

        @Override
        public boolean isTileOccupied() {
            return true;
        }
        @Override
        public Piece getPiece() {
            return this.pieceOnTile;
        }

    }
}
