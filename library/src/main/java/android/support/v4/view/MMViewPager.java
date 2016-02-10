package android.support.v4.view;

import android.content.Context;
import android.util.AttributeSet;

/**
 * <p>MMViewPager</p>
 *
 * <p>Copyright (c) 2014
 *
 * @author Sopra, pole mobilite (Nantes)
 *
 */
public class MMViewPager extends ViewPager{

	/**
	 * Indicates that if the ViewPager should has focus
	 */
	boolean shouldHasFocus;

	/**
	 * Saves the state of the focus
	 */
	boolean hadFocus;

	/**
	 * constructor
	 * @param p_oContext context
	 * @param p_oAttrs attributes set
	 */
	public MMViewPager(Context p_oContext, AttributeSet p_oAttrs) {
		super(p_oContext, p_oAttrs);
	}

	/**
	 * constructor
	 * @param p_oContext context
	 */
	public MMViewPager(Context p_oContext) {
		super(p_oContext);
	}

	@Override
	void populate() {
		//ViewPager should not has focus to call super.populate()
		this.shouldHasFocus = false;
		super.populate();
		this.shouldHasFocus = this.hadFocus;
	}

	@Override
	public boolean hasFocus() {
		//If the ViewPager shouldn't has focus, false is returned and the true fucs state is saved.
		//Otherwise, the super focus state is returned.
		if(!this.shouldHasFocus) {
			this.hadFocus = super.hasFocus();
			return false;
		}
		else  {
			return super.hasFocus();
		}
	}

}
