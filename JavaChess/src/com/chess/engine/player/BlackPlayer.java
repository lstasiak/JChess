package com.chess.engine.player;

import com.chess.engine.Alliance;
import com.chess.engine.board.ChessBoard;
import com.chess.engine.board.Move;
import com.chess.engine.board.Tile;
import com.chess.engine.chess_pieces.Piece;
import com.chess.engine.chess_pieces.Rook;
import com.google.common.collect.ImmutableList;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static com.chess.engine.board.Move.*;

public class BlackPlayer extends Player {

    public BlackPlayer(final ChessBoard chessBoard,
                       final Collection<Move> whiteStandardLegalMoves,
                       final Collection<Move> blackStandardLegalMoves) {

        super(chessBoard, blackStandardLegalMoves, whiteStandardLegalMoves);
    }

    @Override
    public Collection<Piece> getActivePieces() {
        return this.chessBoard.getBlackPieces();
    }

    @Override
    public Alliance getAlliance() {
        return Alliance.BLACK;
    }

    @Override
    public Player getOpponent() {
        return chessBoard.whitePlayer();
    }

    @Override
    protected Collection<Move> calculateKingCastles(final Collection<Move> playerLegals,
                                                    final Collection<Move> opponentLegals) {
        final List<Move> kingCastles = new ArrayList<>();
        // first condition
        if (this.playerKing.isFirstMove() && !this.isInCheck()) {
        // blacks king side castle
            if (!this.chessBoard.getTile(5).isTileOccupied() &&
                    !this.chessBoard.getTile(6).isTileOccupied()) {

                final Tile rookTile = this.chessBoard.getTile(7);
                if (rookTile.isTileOccupied() && rookTile.getPiece().isFirstMove()) {
                    if (Player.calculateAttacksOnTile(5, opponentLegals).isEmpty() &&
                            Player.calculateAttacksOnTile(6, opponentLegals).isEmpty() &&
                            rookTile.getPiece().getPieceType().isRook())
                        kingCastles.add(new KingSideCastleMove(this.chessBoard,
                                                                    this.playerKing,
                                                 6,
                                                                    (Rook)rookTile.getPiece(),
                                                                    rookTile.getTileCoordinate(),
                                                      5));
                }
            }
            if (!this.chessBoard.getTile(1).isTileOccupied() &&
                    !this.chessBoard.getTile(2).isTileOccupied() &&
                    !this.chessBoard.getTile(3).isTileOccupied()) {

                final Tile rookTile = this.chessBoard.getTile(0);
                if (rookTile.isTileOccupied() && rookTile.getPiece().isFirstMove())
                    kingCastles.add(new QueenSideCastleMove(this.chessBoard,
                                                            this.playerKing,
                                         2,
                                                            (Rook)rookTile.getPiece(),
                                                            rookTile.getTileCoordinate(),
                                              3));
            }
        }
        return ImmutableList.copyOf(kingCastles);
    }
}
