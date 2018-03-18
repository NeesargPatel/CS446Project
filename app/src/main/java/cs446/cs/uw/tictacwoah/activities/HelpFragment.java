package cs446.cs.uw.tictacwoah.activities;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import cs446.cs.uw.tictacwoah.R;
import cs446.cs.uw.tictacwoah.models.Board;
import cs446.cs.uw.tictacwoah.models.Piece;
import cs446.cs.uw.tictacwoah.views.BoardView;
import cs446.cs.uw.tictacwoah.views.piece.PieceView;

/**
 * Created by vicdragon on 2018-03-17.
 */

public class HelpFragment extends Fragment {
    public static final String ARG_PAGE = "page";
    public static final String DISPLAY_WIDTH = "width";
    public static final String CONTEXT = "context";

    private int pageNum, displayWidth;
    private Context context;

    public static HelpFragment create(int pageNumber, int displayWidth, Context context) {
        HelpFragment fragment = new HelpFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PAGE, pageNumber);
        args.putInt(DISPLAY_WIDTH, displayWidth);
        fragment.setArguments(args);
        return fragment;
    }

    public HelpFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pageNum = getArguments().getInt(ARG_PAGE);
        displayWidth = getArguments().getInt(DISPLAY_WIDTH);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup view = (ViewGroup) inflater
                .inflate(R.layout.activity_help, container, false);
        TextView text = view.findViewById(R.id.help_text);
        RelativeLayout layout = view.findViewById(R.id.help_layout);
        int num = this.getPageNumber();

        if (num == 0)
            text.setText(R.string.help_text_page1);
        else {
            float cellWidth = displayWidth / Board.SIZE;
            BoardView boardView = new BoardView(getActivity(), 500, cellWidth);
            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
            );
            boardView.setLayoutParams(layoutParams);

            if (num == 1) {
                text.setText(R.string.help_text_page2);
                layout.addView(boardView);
                Piece piece1 = new Piece(0, 0, 1, 1);
                Piece piece2 = new Piece(0, 1, 1, 1);
                Piece piece3 = new Piece(0, 2, 1, 1);
                drawPieceOnBoard(piece1, boardView, layout);
                drawPieceOnBoard(piece2, boardView, layout);
                drawPieceOnBoard(piece3, boardView, layout);
            }
            else if (num == 2) {
                text.setText(R.string.help_text_page3);
                layout.addView(boardView);
                Piece piece1 = new Piece(0, 2, 0, 1);
                Piece piece2 = new Piece(0, 2, 1, 1);
                Piece piece3 = new Piece(0, 2, 2, 1);
                drawPieceOnBoard(piece1, boardView, layout);
                drawPieceOnBoard(piece2, boardView, layout);
                drawPieceOnBoard(piece3, boardView, layout);
            }
            else if (num == 3) {
                text.setText(R.string.help_text_page4);
                layout.addView(boardView);
                Piece piece1 = new Piece(0, 0, 1, 0);
                Piece piece2 = new Piece(0, 1, 1, 1);
                Piece piece3 = new Piece(0, 2, 1, 2);
                drawPieceOnBoard(piece1, boardView, layout);
                drawPieceOnBoard(piece2, boardView, layout);
                drawPieceOnBoard(piece3, boardView, layout);
            }
        }
        return view;
    }

    public int getPageNumber() {
        return pageNum;
    }

    private void drawPieceOnBoard(Piece piece, BoardView boardView, RelativeLayout rootLayout){
        int sizeId = piece.getSizeId();
        int rowId = piece.getRowId();
        int colId = piece.getColId();
        float cellWidth = boardView.getCellWidth();

        float offset = (cellWidth - PieceView.SIZES[sizeId]) / 2;
        float x = cellWidth * rowId + offset;
        float y = boardView.MARGIN_TOP + cellWidth * colId + offset;

        PieceView view = PieceView.getPieceView(getActivity(), (int)x, (int)y,
                PieceView.SIZES[sizeId], PieceView.COLORS[piece.getId()], PieceView.SHAPE.CIRCLE);
        rootLayout.addView(view);
        view.startAnimation();
    }
}