package com.hkm.oc.panel.corepanel.elements.mathmodels;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.Rect;
import android.os.AsyncTask;
import android.util.Log;

import com.hkm.U.Content;
import com.hkm.oc.panel.corepanel.elements.shapes.Element;
import com.hkm.oc.panel.corepanel.elements.Dot;
import com.hkm.datamodel.Label;
import com.hkm.datamodel.LabelRenderObject;
import com.hkm.datamodel.Paths;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by Hesk on
 */
public class Route_render_path_labels extends Element {
    private ArrayList<Dot> dots = new ArrayList<Dot>();
    private ArrayList<Paths> build_paths = new ArrayList<Paths>();
    public static final String TAG = "render_path_labels";
    private final Paint back_drop = new Paint();
    private boolean render_dot_ready;
    private int render_dotlist_total, rendered_dotlist;
    private float percentage_done;

    public Route_render_path_labels(Canvas setCanvas, Context ctx, ArrayList<Paths> bp, ArrayList<Dot> list) {
        super(setCanvas, ctx);

        back_drop.setStyle(Paint.Style.FILL);
        back_drop.setColor(Color.WHITE);
        build_paths = bp;
        dots = list;

        if (build_paths.size() > 0) {
            Content.label_list.clear();
            render_dot_ready = false;
            render_dotlist_total = rendered_dotlist = 0;
            percentage_done = 0f;
            process_paths();
        } else {
            Log.d(TAG, "no build paths found.");
        }
    }

    public void updating() {
        render_dot_ready = false;
    }

    private void label_work_done() {
        rendered_dotlist++;
        percentage_done = rendered_dotlist / render_dotlist_total;
        if (percentage_done == 1.00f) {
            render_dot_ready = true;
        }
    }

    private void process_paths() {
        Iterator<Paths> iPath = build_paths.iterator();
        render_dotlist_total = build_paths.size();
        try {
            while (iPath.hasNext()) {
                Paths path = iPath.next();
                new LabelWorker(path.getPath(), path.getLabel()).execute();
            }
        } catch (Exception e) {
            //last
            Log.d(TAG, "is that the last one now.");
        }
    }


    private Matrix apply_label_matrix;

    private final static int
            in_zone_radius = 50,
            space_between_each_text_label = 60,
            matrix_flags = PathMeasure.POSITION_MATRIX_FLAG | PathMeasure.TANGENT_MATRIX_FLAG;


    private class LabelWorker extends AsyncTask<String, Void, String> {
        private Path p;
        private Label mlabel;
        private float running_distance = 60f;

        public LabelWorker(Path path_e, Label label_e) {
            p = path_e;
            mlabel = label_e;
        }

        protected void onPreExecute() {
            super.onPreExecute();
        }

        /**
         * @return
         * @source http://stackoverflow.com/questions/5286174/getting-coordinates-and-width-height-from-a-matrix
         */
        protected String doInBackground(String... args) {
            final PathMeasure meas = new PathMeasure(p, false);
            //get total length of this path
            final float length = meas.getLength();
            final int text_width = mlabel.get_text_bound().x;
            final int advancing = text_width + space_between_each_text_label;
            final Matrix label_position_matrix = new Matrix();

            if (advancing < length) {
                while (running_distance < length) {
                    meas.getMatrix(running_distance, label_position_matrix, matrix_flags);
                    float[] val = new float[9];
                    label_position_matrix.getValues(val);
                    PointF dposition = new PointF(val[Matrix.MTRANS_X], val[Matrix.MTRANS_Y]);
                    //   width = val[Matrix.MSCALE_X] * imageWidth;
                    //   height = val[Matrix.MSCALE_Y] * imageHeight;
                    //   boolean fte = lookup_point_in_zone(dposition);
                    if (lookup_point_in_zone(dposition)) {
                        // label_position_matrix.preConcat(ctx.getMpanelmatrix());
                        Content.label_list.add(new LabelRenderObject(mlabel, label_position_matrix));
                    }
                    running_distance += advancing;
                }
            }
            return "";
        }

        protected void onPostExecute(String str_n) {
            // dismiss the dialog after getting all products
            // updating UI from Background Thread
            // runOnUiThread(new Runnable() {
            //     public void run() {
            //finally show your button
            //    }
            // });
            label_work_done();
        }
    }


    private boolean lookup_point_in_zone(PointF poi) {
        for (Dot dot : dots) {
            if (EQPool.onPressedCicle(dot, poi, in_zone_radius)) {
                return false;
            }
        }
        return true;
    }

    private void render_label_path() {
        if (render_dot_ready) {
            for (LabelRenderObject lb : Content.label_list) {
                final Paint tex = lb.getLbl().get_line_label_paint();
                final String labelStr = lb.getLbl().get_line_label_text();
                final Matrix label_position_matrix = new Matrix();
                label_position_matrix.set(lb.getMatrix());
                /* got the matrix first then apply to the new one */
                label_position_matrix.postConcat(apply_label_matrix);
                // tex textPaint = textView.getPaint();
                // tex.getTextBounds(labelStr, 0, labelStr.length(), bounds);
                final Point ptBound = lb.getLbl().get_text_bound();
                render_internal(main, label_position_matrix, ptBound, tex, labelStr);
            }
        }

    }

    public void render_label_path(Canvas cus, Matrix output_matrix) throws Exception {
        if (render_dot_ready) {
            for (LabelRenderObject lb : Content.label_list) {
                final Paint tex = lb.getLbl().get_line_label_paint();
                final String labelStr = lb.getLbl().get_line_label_text();
                final Matrix label_position_matrix = new Matrix();
                label_position_matrix.set(lb.getMatrix());
                /* got the matrix first then apply to the new one */
                label_position_matrix.postConcat(output_matrix);
                // tex textPaint = textView.getPaint();
                // tex.getTextBounds(labelStr, 0, labelStr.length(), bounds);
                final Point ptBound = lb.getLbl().get_text_bound();
                render_internal(cus, label_position_matrix, ptBound, tex, labelStr);
            }
        } else {
            throw new Exception("cannot render the labels. render dot is not ready.");
        }
    }

    private void render_internal(Canvas c, Matrix m, Point ptBound, Paint tex, String label_str) {
        c.save();
        c.setMatrix(m);
        c.drawRect(new Rect(0, -ptBound.y, ptBound.x, ptBound.y), back_drop);
        c.drawText(label_str, 0, ptBound.y, tex);
        c.restore();
    }

    public void change_matrix(Matrix m) {
        apply_label_matrix = m;
    }

    @Override
    public void rendering() {
        render_label_path();
    }

}
