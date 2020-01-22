package com.chess.engine.player;

import com.chess.engine.Alliance;
import com.chess.engine.board.ChessBoard;
import com.chess.engine.board.Move;
import com.chess.engine.chess_pieces.Piece;

import java.util.Collection;

public class BlackPlayer extends Player {

    public BlackPlayer(ChessBoard chessBoard,
                       Collection<Move> whiteStandardLegalMoves,
                       Collection<Move> blackStandardLegalMoves) {

        super(chessBoard, blackStandardLegalMoves, whiteStandardLegalMoves);
    }

    @Override
    protected Collection<Piece> getActivePieces() {
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
}
