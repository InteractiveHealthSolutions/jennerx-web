package org.ird.android.epi.common;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;

import org.ird.android.epi.R;
import org.ird.android.epi.alert.IDialogListener;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.NumberPicker;
import android.widget.Spinner;


public class EpiUtils extends Activity
{
	private static SimpleDateFormat dateFormat = new SimpleDateFormat(GlobalConstants.DATE_FORMAT);
	private static String serverAddress = null;
	private static SharedPreferences preferences;
	private static final String TAG_EPI_UTILS = "EpiUtils";

	public static boolean setNumberPickerValues(NumberPicker np, int minimum, int maximum)
	{
		try
		{
			String[] nums = new String[maximum];
			for (int i = minimum; i <= nums.length; i++)
				nums[i] = Integer.toString(i);

			np.setMinValue(minimum);
			np.setMaxValue(maximum);
			np.setWrapSelectorWheel(false);
			np.setDisplayedValues(nums);
			np.setValue(minimum);
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
			return false;
		}
		return true;
	}

	public static SimpleDateFormat getDateFormat()
	{
		return dateFormat;
	}

	public static Date getDate(String date)
	{
		if (date == null || "".equalsIgnoreCase(date.trim()))
		{
			Log.e(TAG_EPI_UTILS, "");
			return null;
		}
		try
		{
			return dateFormat.parse(date);
		}
		catch (ParseException e)
		{
			e.printStackTrace();
			Log.e(TAG_EPI_UTILS, e.getMessage());
			return null;
		}
		catch (Exception e)
		{
			e.printStackTrace();
			Log.e(TAG_EPI_UTILS, e.getMessage());
			return null;
		}

	}

	public static boolean isPastDate(Date d)
	{
		if (d.getTime() < new Date().getTime())
		{
			return true;
		}
		else
		{
			return true;
		}
	}

	public static boolean isPastDate(Calendar c)
	{
		Date d = c.getTime();
		if (d.getTime() < new Date().getTime())
		{
			return true;
		}
		else
		{
			return true;
		}
	}

	public static int getIndexByValue(Spinner spinner, String value)
	{
		// Set default index to -1
		int index = -1;

		for (int i = 0; i < spinner.getCount(); i++)
		{
			if (spinner.getItemAtPosition(i).toString().equals(value))
			{
				index = i;
			}
		}
		return index;
	}

	public static AlertDialog showAlert(Context context, String messageToShow, String title, final IDialogListener listener)
	{
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setIcon(R.drawable.alert);
		builder.setTitle(title.toUpperCase());
		builder.setPositiveButton("Ok", new OnClickListener()
		{

			@Override
			public void onClick(DialogInterface dialog, int which)
			{
				dialog.dismiss();
				listener.onDialogPositiveClick();
			}
		});
		AlertDialog alert = builder.create();
		alert.setMessage(messageToShow);
		alert.setTitle(title);
		return alert;
	}

	public static AlertDialog showDismissableDialog(Context context, String messageToShow, String title)
	{
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setIcon(R.drawable.alert);
		builder.setTitle(title.toUpperCase());
		builder.setPositiveButton("Ok", new OnClickListener()
		{

			@Override
			public void onClick(DialogInterface dialog, int which)
			{
				dialog.dismiss();
			}
		});
		AlertDialog alert = builder.create();
		alert.setMessage(messageToShow);
		alert.setTitle(title);
		return alert;
	}

	public static AlertDialog showServerSideError(Context context, Integer responseCode, final IDialogListener listener)
	{
		AlertDialog d = showAlert(context, context.getString(R.string.error_server_side) + responseCode, "Error", listener);
		return d;
	}

	public static AlertDialog showYesNoDialog(Context context, String messageToShow, String title, String positiveButtonText, String negativeButtonText, final IDialogListener listener)
	{
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setIcon(R.drawable.alert);
		builder.setTitle(title.toUpperCase());
		if (positiveButtonText == null)
		{
			positiveButtonText = "Ok";
		}
		if (negativeButtonText == null)
		{
			negativeButtonText = "Cancel";
		}

		builder.setPositiveButton(positiveButtonText, new OnClickListener()
		{

			@Override
			public void onClick(DialogInterface dialog, int which)
			{
				dialog.dismiss();
				listener.onDialogPositiveClick();
			}
		});

		builder.setNegativeButton(negativeButtonText, new OnClickListener()
		{

			@Override
			public void onClick(DialogInterface dialog, int which)
			{
				dialog.dismiss();
				listener.onDialogNegativeClick();
			}
		});

		AlertDialog alert = builder.create();
		alert.setMessage(messageToShow);

		return alert;
	}

	public static AlertDialog showYesNoDialog(Context context, String messageToShow, String title)
	{
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setIcon(R.drawable.alert);
		builder.setTitle(title.toUpperCase());
		AlertDialog alert = builder.create();
		alert.setMessage(messageToShow);


		return alert;
	}

	public String getServerAddress(Context cxt)
	{
		if (preferences == null)
		{
			preferences = PreferenceManager.getDefaultSharedPreferences(cxt);
		}
		// use complete server URL
		boolean useServerAdress = preferences.getBoolean("tokenized_url", false);
		if (useServerAdress)
		{
			serverAddress = preferences.getString("fully_qualifified_url", "");
		}
		// Construct the address using the individual components of the URL
		else
		{
			String protocol = preferences.getString("protocol_pref", cxt.getResources().getString(R.string.protocol_http_value));
			String ip = preferences.getString("ip_pref", "125.209.94.150");
			String port = preferences.getString("port_pref", "7000");
			String appName = preferences.getString("appname_pref", "unfepi").trim() + "/serv";

			serverAddress = protocol + ip.trim() + ":" + port.trim() + "/" + appName.trim();
		}
		return serverAddress;
	}

	public static void showLottery(Context form, Map<String, String> lotteries, IDialogListener listener)
	{
		// Create an AlertDialog, set OK button and handle Click event by dismissing it
		AlertDialog.Builder builder = new AlertDialog.Builder(form);
		builder.setPositiveButton("Ok", new AlertDialog.OnClickListener()
		{

			@Override
			public void onClick(DialogInterface dialog, int which)
			{
				dialog.dismiss();
			}
		});

		// Process Map containing lottery result.
		StringBuilder codesAndAmount;
		if (lotteries.size() > 0)
		{
			codesAndAmount = new StringBuilder();
			Iterator<String> iter = lotteries.keySet().iterator();
			while (iter.hasNext())
			{
				String code = iter.next();
				codesAndAmount.append("Code: " + code + "\n Amount: " + lotteries.get(code) + "\n");
			}
			builder.setMessage("The amount and codes are as follows: \n" + codesAndAmount.toString());
			builder.setTitle("Lottery Won!");
		}
		else
		{
			builder.setMessage("Sorry you lost the lottery");
			builder.setTitle("Lottery Lost!");
		}
		builder.show();
	}

	public static JSONArray mergeJsonArrays(JSONArray... arrays)
	{
		JSONArray toReturn = new JSONArray();
		for (JSONArray j : arrays)
		{
			for (int i = 0; i < j.length(); i++)
			{
				try
				{
					toReturn.put(j.get(i));
				}
				catch (JSONException e)
				{

					e.printStackTrace();
				}
			}

		}

		return toReturn;
	}

	public static boolean stringToBool(String s)
	{
		if (s.equals("1") || s.equalsIgnoreCase("true"))
		{
			return true;
		}

		if (s.equals("0") || s.equalsIgnoreCase("false"))
		{
			return false;
		}

		else
		{
			throw new IllegalArgumentException(s + " is not a bool. Only 1 and 0 are.");
		}
	}

	/**Convert Dp to px
	 * @param context
	 * @param dp
	 * @return
	 */
	public static Integer convertDptoPx(Context context, Integer dp)
	{
		final float scale = context.getResources().getDisplayMetrics().density;
		int px = (int) (dp * scale + 0.5f);

		return px;
	}

}
