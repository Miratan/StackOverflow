package mlehmkuhl.stackoverflow.ui;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;

import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;
import com.bumptech.glide.load.resource.bitmap.TransformationUtils;

public class CircleTransform extends BitmapTransformation {

	public CircleTransform(Context context) {
		super(context);
	}

	@Override
	protected Bitmap transform(BitmapPool pool, Bitmap toTransform, int outWidth, int outHeight) {
		if (toTransform == null) {
			return null;
		}

		final Bitmap toReuse = pool.get(outWidth, outHeight, toTransform.getConfig() != null ? toTransform.getConfig() : Bitmap.Config.ARGB_8888);
		Bitmap transformed = TransformationUtils.centerCrop(toReuse, toTransform, outWidth, outHeight);
		if (toReuse != null && toReuse != transformed && !pool.put(toReuse)) {
			toReuse.recycle();
		}

		int size = Math.min(outWidth, outHeight);
		float r = size / 2f;

		Bitmap result = pool.get(size, size, Bitmap.Config.ARGB_8888);
		if (result == null) {
			result = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888);
		}

		Paint paint = new Paint();
		paint.setShader(new BitmapShader(transformed, BitmapShader.TileMode.CLAMP, BitmapShader.TileMode.CLAMP));
		paint.setAntiAlias(true);

		Canvas canvas = new Canvas(result);
		canvas.drawCircle(r, r, r, paint);

		return result;
	}

	@Override
	public String getId() {
		return getClass().getName();
	}
}
