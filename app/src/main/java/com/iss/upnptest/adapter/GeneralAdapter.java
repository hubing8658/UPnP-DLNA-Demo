package com.iss.upnptest.adapter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * 通用Adapter基类，其他要实现Adapter的只要继承此类，实现convert方法即可
 * 
 * @author hubing
 * @version 1.0.0 2015-4-10
 */
public abstract class GeneralAdapter<E> extends BaseAdapter {

	private int maxCount = 200; // ListView中最大显示数据条数
	private List<E> data;
	private int resource; // ListView中Item布局id
	private Context ctx;

	private final Object mLock = new Object();

	/**
	 * 
	 * @param resource
	 *            item布局文件
	 * @param data
	 */
	public GeneralAdapter(Context ctx, int resource, List<E> data) {
		this.ctx = ctx;
		this.resource = resource;
		setData(data);
	}

	/**
	 * 设置Adapter中的数据
	 * 
	 * @param data
	 */
	public void setData(List<E> data) {
		synchronized (mLock) {
			if (data == null) {
				data = new ArrayList<E>();
			}
		}
		this.data = data;
		checkListSize();
	}

	public List<E> getData() {
		return data;
	}

	/**
	 * 向Adapter中List添加单个对象
	 * 
	 * @author hubing
	 * @param object
	 */
	public void add(E object) {
		synchronized (mLock) {
			if (data != null) {
				data.add(object);
			}
			checkListSize();
		}
	}
	
	/**
	 * 向Adapter中List末尾追加列表数据
	 * 
	 * @param data
	 */
	public void addAll(Collection<? extends E> collection) {
		synchronized (mLock) {
			if (data != null) {
				data.addAll(data);
				checkListSize();
			}
		}
	}

	/**
     * Inserts the specified object at the specified index in the array.
     *
     * @param object The object to insert into the array.
     * @param index The index at which the object must be inserted.
     */
	public void insert(E object, int index) {
        synchronized (mLock) {
            if (data != null) {
                data.add(index, object);
            }
        }
        notifyDataSetChanged();
    }
	
	/**
     * Removes the specified object from the array.
     *
     * @param object The object to remove.
     */
    public void remove(E object) {
        synchronized (mLock) {
            if (data != null) {
            	data.remove(object);
            }
        }
        notifyDataSetChanged();
    }

    /**
     * Remove all elements from the list.
     */
    public void clear() {
        synchronized (mLock) {
            if (data != null) {
            	data.clear();
            }
        }
        notifyDataSetChanged();
    }
	
    /**
     * Sorts the content of this adapter using the specified comparator.
     *
     * @param comparator The comparator used to sort the objects contained
     *        in this adapter.
     */
    public void sort(Comparator<? super E> comparator) {
        synchronized (mLock) {
            if (data != null) {
                Collections.sort(data, comparator);
            }
        }
        notifyDataSetChanged();
    }

	/**
	 * 检测当前List中的数据是否超过最大值maxCount
	 */
	private void checkListSize() {
		int totalCount = data.size();
		if (totalCount > maxCount) {
			data = data.subList(totalCount - maxCount, totalCount);
		}
		this.notifyDataSetChanged();
	}

	/**
	 * 获取ListView中显示的item最大条数
	 * 
	 * @return maxCount
	 */
	public int getMaxCount() {
		return maxCount;
	}

	/**
	 * 设置ListView中显示的item最大条数(此方法必需在setData方法之前调用才有效)
	 * 
	 * @param maxCount
	 */
	public void setMaxCount(int maxCount) {
		this.maxCount = maxCount;
	}

	/**
     * Returns the context associated with this array adapter. The context is used
     * to create views from the resource passed to the constructor.
     *
     * @return The Context associated with this adapter.
     */
    public Context getContext() {
        return ctx;
    }
	
	@Override
	public int getCount() {
		return this.data.size();
	}

	@Override
	public E getItem(int position) {
		return data.get(position);
	}

	/**
     * Returns the position of the specified item in the array.
     *
     * @param item The item to retrieve the position of.
     *
     * @return The position of the specified item.
     */
    public int getPosition(E item) {
        return data.indexOf(item);
    }
	
	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = ViewHolder.get(ctx, resource, convertView);
		convert(holder, getItem(position), position);
		return holder.getConvertView();
	}

	/**
	 * 实现此方法，完成item界面设置
	 */
	public abstract void convert(ViewHolder holder, E item, int position);

	/**
	 * 
	 * @author hubing
	 * @version 1.0.0 2015-4-10
	 */
	public static class ViewHolder {

		private View convertView;
		private SparseArray<View> views;

		private ViewHolder(Context ctx, int resource) {
			convertView = LayoutInflater.from(ctx).inflate(resource, null);
			convertView.setTag(this);
			views = new SparseArray<View>();
		}

		/**
		 * 获取ViewHoler
		 * 
		 * @param ctx
		 * @param resource
		 * @param convertView
		 * @return
		 */
		public static ViewHolder get(Context ctx, int resource, View convertView) {
			ViewHolder vh = null;
			if (convertView == null) {
				vh = new ViewHolder(ctx, resource);
			} else {
				vh = (ViewHolder) convertView.getTag();
			}
			return vh;
		}

		public View getConvertView() {
			return this.convertView;
		}

		/**
		 * 从ViewHoler中获取已经保持的View
		 * 
		 * @param resId
		 * @return
		 */
		public View getView(int widgetId) {
			View view = views.get(widgetId);
			if (view == null) {
				view = convertView.findViewById(widgetId);
				views.append(widgetId, view);
			}
			return view;
		}

		/**
		 * 设置继承自TextView的控件文本
		 * 
		 * @param widgetId
		 * @param text
		 */
		public void setText(int widgetId, String text) {
			TextView tv = (TextView) getView(widgetId);
			if (tv != null && !TextUtils.isEmpty(text)) {
				tv.setText(text);
			}
		}

		/**
		 * 设置继承自TextView的控件文本
		 * 
		 * @param widgetId
		 * @param resid
		 */
		public void setText(int widgetId, int resid) {
			TextView tv = (TextView) getView(widgetId);
			if (tv != null) {
				tv.setText(resid);
			}
		}

		/**
		 * 设置背景颜色
		 * 
		 * @param widgetId
		 * @param color
		 */
		public void setBackgroundColor(int widgetId, int color) {
			View view = getView(widgetId);
			if (view != null) {
				view.setBackgroundColor(color);
			}
		}

		/**
		 * 设置背景颜色
		 * 
		 * @param widgetId
		 * @param resid
		 */
		public void setBackgroundResource(int widgetId, int resid) {
			View view = getView(widgetId);
			if (view != null) {
				view.setBackgroundResource(resid);
			}
		}

		/**
		 * ImageView设置图片
		 * 
		 * @param widgetId
		 * @param drawable
		 */
		public void setImageDrawable(int widgetId, Drawable drawable) {
			ImageView iv = (ImageView) getView(widgetId);
			if (iv != null) {
				iv.setImageDrawable(drawable);
			}
		}

		/**
		 * ImageView设置图片
		 * 
		 * @param widgetId
		 * @param resid
		 */
		public void setImageResource(int widgetId, int resid) {
			ImageView iv = (ImageView) getView(widgetId);
			if (iv != null) {
				iv.setImageResource(resid);
			}
		}

		/**
		 * ImageView设置图片
		 * 
		 * @param widgetId
		 * @param bm
		 */
		public void setImageBitmap(int widgetId, Bitmap bm) {
			ImageView iv = (ImageView) getView(widgetId);
			if (iv != null) {
				iv.setImageBitmap(bm);
			}
		}

	}

}
