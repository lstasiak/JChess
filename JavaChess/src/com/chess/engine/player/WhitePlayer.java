package com.chess.engine.player;


import com.chess.engine.Alliance;
import com.chess.engine.board.ChessBoard;
import com.chess.engine.board.Move;
import com.chess.engine.board.Move.KingSideCastleMove;
import com.chess.engine.board.Tile;
import com.chess.engine.chess_pieces.Piece;
import com.chess.engine.chess_pieces.Rook;
import com.google.common.collect.ImmutableList;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static com.chess.engine.board.Move.*;

public class WhitePlayer extends Player {

    public WhitePlayer(final ChessBoard chessBoard,
                       final Collection<Move> whiteStandardLegalMoves,
                       final Collection<Move> blackStandardLegalMoves) {

        super(chessBoard, whiteStandardLegalMoves, blackStandardLegalMoves);
    }

    @Override
    public Collection<Piece> getActivePieces() {
        return this.chessBoard.getWhitePieces();
    }

    @Override
    public Alliance getAlliance() {
        return Alliance.WHITE;
    }

    @Override
    public Player getOpponent() {
        return this.chessBoard.blackPlayer();
    }

    @Override
    protected Collection<Move> calculateKingCastles(final Collection<Move> playerLegals,
                                                    final Collection<Move> opponentLegals) {
        final List<Move> kingCastles = new ArrayList<>();
        // first condition
        if (this.playerKing.isFirstMove() && !this.isInCheck()) {
            // whites king side castle
            if (!this.chessBoard.getTile(61).isTileOccupied() &&
                    !this.chessBoard.getTile(62).isTileOccupied()) {

                final Tile rookTile = this.chessBoard.getTile(63);
                if (rookTile.isTileOccupied() && rookTile.getPiece().isFirstMove()) {
                    if (Player.calculateAttacksOnTile(61, opponentLegals).isEmpty() &&
                            Player.calculateAttacksOnTile(62, opponentLegals).isEmpty() &&
                            rookTile.getPiece().getPieceType().isRook())
                        kingCastles.add(new KingSideCastleMove(this.chessBoard,
                                                                    this.playerKing,
                                                 62,
                                                                    (Rook)rookTile.getPiece(),
                                                                    rookTile.getTileCoordinate(),
                                                      61));
                }
            }
            if (!this.chessBoard.getTile(59).isTileOccupied() &&
                !this.chessBoard.getTile(58).isTileOccupied() &&
                !this.chessBoard.getTile(57).isTileOccupied()) {

                final Tile rookTile = this.chessBoard.getTile(56);
                if (rookTile.isTileOccupied() && rookTile.getPiece().isFirstMove())
                    kingCastles.add(new QueenSideCastleMove(this.chessBoard,
                                                                 this.playerKing,
                                              58,
                                                                 (Rook)rookTile.getPiece(),
                                                                 rookTile.getTileCoordinate(),
                                                   59));
            }
        }
        return ImmutableList.copyOf(kingCastles);
    }

}
