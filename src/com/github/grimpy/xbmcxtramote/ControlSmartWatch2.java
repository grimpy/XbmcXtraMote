package com.github.grimpy.xbmcxtramote;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import com.sonyericsson.extras.liveware.aef.control.Control;
import com.sonyericsson.extras.liveware.extension.util.control.ControlExtension;
import com.sonyericsson.extras.liveware.extension.util.control.ControlObjectClickEvent;
import com.sonyericsson.extras.liveware.extension.util.control.ControlView;
import com.sonyericsson.extras.liveware.extension.util.control.ControlView.OnClickListener;
import com.sonyericsson.extras.liveware.extension.util.control.ControlViewGroup;
import com.github.grimpy.xbmcxtramote.R;

/**
 * The sample control for SmartWatch handles the control on the accessory. This
 * class exists in one instance for every supported host application that we
 * have registered to
 */
class ControlSmartWatch2 extends ControlExtension {

    private static final int ANIMATION_DELTA_MS = 500;
    private static final int SELECT_TOGGLER_MS = 2000;
    private static final int MENU_ITEM_FAV = 0;
    private static final int MENU_ITEM_CONTEXT = 1;
    private static final int MENU_ITEM_HOME = 2;
    private static final int MENU_ITEM_3 = 3;
    private static final int MENU_ITEM_4 = 4;
    private static final int MENU_ITEM_5 = 5;
    
    private int mActiveLayout = 0;
    private int[] mLayoutids =  {R.layout.navigate, R.layout.playback};

    private Handler mHandler;
    private XBMCClient mXBMC;

    private List<ControlViewGroup> mLayouts = new ArrayList<ControlViewGroup>();

    private boolean mTextMenu = false;
    Bundle[] mMenuItemsText = new Bundle[3];
    Bundle[] mMenuItemsIcons = new Bundle[3];

    /**
     * Create sample control.
     *
     * @param hostAppPackageName Package name of host application.
     * @param context The context.
     * @param handler The handler to use
     */
    ControlSmartWatch2(final String hostAppPackageName, final Context context,
            Handler handler) {
        super(context, hostAppPackageName);
        Log.d(MyExtensionService.LOG_TAG, "Init controlsmartwatch " + hostAppPackageName);

        if (handler == null) {
            throw new IllegalArgumentException("handler == null");
        }
        mHandler = handler;
        mXBMC = new XBMCClient("http://192.168.1.3:8080/jsonrpc");
        setupClickables(context);
        initializeMenus();
    }

    private void initializeMenus() {
        // TODO: Is this supported?
        mMenuItemsText[0] = new Bundle();
        mMenuItemsText[0].putInt(Control.Intents.EXTRA_MENU_ITEM_ID, MENU_ITEM_FAV);
        mMenuItemsText[0].putString(Control.Intents.EXTRA_MENU_ITEM_TEXT, "Favourites");
        mMenuItemsText[1] = new Bundle();
        mMenuItemsText[1].putInt(Control.Intents.EXTRA_MENU_ITEM_ID, MENU_ITEM_CONTEXT);
        mMenuItemsText[1].putString(Control.Intents.EXTRA_MENU_ITEM_TEXT, "Context");
        mMenuItemsText[2] = new Bundle();
        mMenuItemsText[2].putInt(Control.Intents.EXTRA_MENU_ITEM_ID, MENU_ITEM_HOME);
        mMenuItemsText[2].putString(Control.Intents.EXTRA_MENU_ITEM_TEXT, "Home");

    }

    /**
     * Get supported control width.
     *
     * @param context The context.
     * @return the width.
     */
    public static int getSupportedControlWidth(Context context) {
        return context.getResources().getDimensionPixelSize(R.dimen.smart_watch_2_control_width);
    }

    /**
     * Get supported control height.
     *
     * @param context The context.
     * @return the height.
     */
    public static int getSupportedControlHeight(Context context) {
        return context.getResources().getDimensionPixelSize(R.dimen.smart_watch_2_control_height);
    }

    @Override
    public void onDestroy() {
        Log.d(MyExtensionService.LOG_TAG, "onDestroy");
        mHandler = null;
    };

    @Override
    public void onStart() {
        // Nothing to do. Animation is handled in onResume.
    }

    @Override
    public void onStop() {
        // Nothing to do. Animation is handled in onPause.
    }

    @Override
    public void onResume() {
        showActiveLayout();
    }
    
    private void showActiveLayout() {
        Bundle[] data = new Bundle[4];
        showLayout(mLayoutids[mActiveLayout], data);
    }

    @Override
    public void onSwipe(int direction) {
        // TODO Auto-generated method stub
        super.onSwipe(direction);
        if (direction == Control.Intents.SWIPE_DIRECTION_LEFT) {
            mActiveLayout--;
        } else if (direction == Control.Intents.SWIPE_DIRECTION_RIGHT) {
            mActiveLayout++;
        }
        if (mActiveLayout < 0) {
            mActiveLayout = mLayoutids.length -1;
        } else if (mActiveLayout >= mLayoutids.length) {
            mActiveLayout = 0;
        }
        showActiveLayout();
    }

    @Override
    public void onObjectClick(final ControlObjectClickEvent event) {
        Log.d(MyExtensionService.LOG_TAG, "onObjectClick() " + event.getClickType());
        if (event.getLayoutReference() != -1) {
            mLayouts.get(mActiveLayout).onClick(event.getLayoutReference());
        }
    }

    @Override
    public void onKey(final int action, final int keyCode, final long timeStamp) {
        Log.d(MyExtensionService.LOG_TAG, "onKey()");
        if (action == Control.Intents.KEY_ACTION_RELEASE
                && keyCode == Control.KeyCodes.KEYCODE_OPTIONS) {
            showMenu(mMenuItemsText);
        }
        else if (action == Control.Intents.KEY_ACTION_RELEASE
                && keyCode == Control.KeyCodes.KEYCODE_BACK) {
            Log.d(MyExtensionService.LOG_TAG, "onKey() - back button intercepted.");
        }
    }

    @Override
    public void onMenuItemSelected(final int menuItem) {
        Log.d(MyExtensionService.LOG_TAG, "onMenuItemSelected() - menu item " + menuItem);
        if (menuItem == MENU_ITEM_FAV) {
            mXBMC.showFavourites();
        } else if (menuItem == MENU_ITEM_CONTEXT) {
            mXBMC.showContextMenu();
        } else if (menuItem == MENU_ITEM_HOME) {
            mXBMC.showHome();
        }
    }

    private void setupClickables(Context context) {
        ControlViewGroup layout;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService
                (Context.LAYOUT_INFLATER_SERVICE);
        View navigateview = inflater.inflate(R.layout.navigate
                , null);
        layout = (ControlViewGroup) parseLayout(navigateview);
        mLayouts.add(layout);
        if (layout != null) {
            ControlView left = layout.findViewById(R.id.left_arrow);
            left.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick() {
                    mXBMC.sendLeft();
                }
            });
            ControlView up = layout.findViewById(R.id.up_arrow);
            up.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick() {
                    mXBMC.sendUp();
                }
            });
            ControlView down = layout.findViewById(R.id.down_arrow);
            down.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick() {
                    mXBMC.sendDown();
                }
            });
            ControlView right = layout.findViewById(R.id.right_arrow);
            right.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick() {
                    mXBMC.sendRight();
                }
            });
            ControlView tv = layout.findViewById(R.id.tvshow);
            tv.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick() {
                    mXBMC.showTvShows();
                }
            });
            ControlView movies = layout.findViewById(R.id.movies);
            movies.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick() {
                    mXBMC.showMovies();
                }
            });
            ControlView select = layout.findViewById(R.id.select);
            select.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick() {
                    mXBMC.sendSelect();
                }
            });
            ControlView back = layout.findViewById(R.id.back_arrow);
            back.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick() {
                    mXBMC.sendBack();
                }
            });
            ControlView info = layout.findViewById(R.id.info);
            info.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick() {
                    mXBMC.askInfo();
                }
            });
        }
        View playback = inflater.inflate(R.layout.playback, null);
        layout = (ControlViewGroup) parseLayout(playback);
        mLayouts.add(layout);
        ControlView stop = layout.findViewById(R.id.stop);
        stop.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick() {
                mXBMC.stop();
            }
        });
        ControlView pause = layout.findViewById(R.id.pause);
        pause.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick() {
                mXBMC.playPause();
            }
        });
        ControlView voldown = layout.findViewById(R.id.voldown);
        voldown.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick() {
                mXBMC.decreaseVolume();
            }
        });
        ControlView mute = layout.findViewById(R.id.mute);
        mute.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick() {
                mXBMC.mute();
            }
        });
        ControlView volup = layout.findViewById(R.id.volup);
        volup.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick() {
                mXBMC.increaseVolume();
            }
        });
        ControlView fwd = layout.findViewById(R.id.fwd);
        fwd.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick() {
                mXBMC.forward();
            }
        });
        ControlView rew = layout.findViewById(R.id.rew);
        rew.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick() {
                mXBMC.rewind();
            }
        });
        ControlView next = layout.findViewById(R.id.next);
        next.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick() {
                mXBMC.next();
            }
        });
        ControlView prev = layout.findViewById(R.id.prev);
        prev.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick() {
                mXBMC.previous();
            }
        });



    };

}
