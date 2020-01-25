package com.chess.engine.board;

import com.chess.engine.Alliance;
import com.chess.engine.chess_pieces.*;
import com.chess.engine.player.BlackPlayer;
import com.chess.engine.player.Player;
import com.chess.engine.player.WhitePlayer;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class ChessBoard {

    private final List<Tile> gameBoard;
    private final Collection<Piece> whitePieces;
    private final Collection<Piece> blackPieces;

    private final WhitePlayer whitePlayer;
    private final BlackPlayer blackPlayer;
    private final Player currentPlayer;

    public ChessBoard(final Builder builder) {
        this.gameBoard = createGameBoard(builder);
        this.whitePieces = calculateActivePieces(this.gameBoard, Alliance.WHITE);
        this.blackPieces = calculateActivePieces(this.gameBoard, Alliance.BLACK);

        final Collection<Move> whiteStandardLegalMoves = calculateLegalMoves(this.whitePieces);
        final Collection<Move> blackStandardLegalMoves = calculateLegalMoves(this.blackPieces);

        this.whitePlayer = new WhitePlayer(this, whiteStandardLegalMoves, blackStandardLegalMoves);
        this.blackPlayer = new BlackPlayer(this, whiteStandardLegalMoves, blackStandardLegalMoves);
        this.currentPlayer = builder.nextMoveMaker.choosePlayer(this.blackPlayer, this.whitePlayer);
    }

    @Override
    public String toString(){
        final StringBuilder builder = new StringBuilder();
        for(int i = 0; i < BoardUtils.NUM_TILES; i++){
            final String tileText = this.gameBoard.get(i).toString();
            builder.append(String.format("%3s", tileText));
            if ((i+1) % BoardUtils.NUM_TILES_PER_ROW == 0)
                builder.append("\n");
        }
        return builder.toString();
    }

    private Collection<Move> calculateLegalMoves(final Collection<Piece> pieces) {
        final List<Move> legalMoves = new ArrayList<>();

        pieces.stream()
                .map(piece -> piece.calculateLegalMoves(this))
                .forEach(legalMoves::addAll);

        return ImmutableList.copyOf(legalMoves);
    }

    private static Collection<Piece> calculateActivePieces(final List<Tile> gameBoard, final Alliance alliance) {

        final List<Piece> activePieces = gameBoard.stream()
                .filter(Tile::isTileOccupied)
                .map(Tile::getPiece)
                .filter(piece -> piece.getPieceAlliance() == alliance)
                .collect(Collectors.toList());

        return ImmutableList.copyOf(activePieces);
    }

    private static List<Tile> createGameBoard(final Builder builder) {

        final Tile[] tiles = IntStream.range(0, BoardUtils.NUM_TILES)
                .mapToObj(i -> Tile.createTile(i, builder.boardConfig.get(i)))
                .toArray(Tile[]::new);

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

    private static void generatePawns(final Builder builder, final Alliance alliance, int firstPosition, int lastPosition) {

        IntStream.rangeClosed(firstPosition, lastPosition)
                .mapToObj(i -> new Pawn(i, alliance))
                .forEach(builder::setPiece);
    }

    public Tile getTile(final int tileCoordinate) {
        return gameBoard.get(tileCoordinate);
    }

    public Collection<Piece> getBlackPieces() {
        return this.blackPieces;
    }
    public Collection<Piece> getWhitePieces() {
        return this.whitePieces;
    }

    public Player blackPlayer() {
        return this.blackPlayer;
    }

    public Player whitePlayer() {
        return this.whitePlayer;
    }

    public Player currentPlayer(){
        return this.currentPlayer;
    }

    public Collection<Move> getAllLegalMoves() {
        Iterable<Move> allMoves = Iterables.unmodifiableIterable(Iterables.concat(this.whitePlayer.getLegalMoves(),
                this.blackPlayer.getLegalMoves()));

        return ImmutableList.copyOf(allMoves);
    }

    public static class Builder {

        Map<Integer, Piece> boardConfig;
        Alliance nextMoveMaker;
        Pawn enPassantPawn;

        public Builder() {
            this.boardConfig = new HashMap<>();
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

        public void setEnPassantPawn(Pawn movedPawn) {
            this.enPassantPawn = movedPawn;
        }
    }

}
