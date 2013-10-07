package com.jackhxs.remote;

import android.os.Parcel;
import android.os.Parcelable;

public final class Constants {
	/** Status Constants */
	public static final int STATUS_RUNNING = 0;
	public static final int STATUS_FINISHED = 1;
	public static final int STATUS_ERROR  = 2;

	/** REST Interface Constants */
	public enum Operation implements Parcelable{
		POST_SIGNUP,
		POST_LOGIN,
		GET_CARDS,
		GET_CONTACTS,
		GET_BOTH_CONTACT_AND_CARD,
		POST_CARD,
		PUT_CARD,
		POST_CONTACT,
		DEL_CONTACT,
		DEL_CARD,
        REFER,
        LIST_REFERRALS;

		@Override
		public int describeContents() {
			return 0;
		}

		@Override
		public void writeToParcel(final Parcel dest, final int flags) {
			dest.writeInt(ordinal());
		}

		public static final Creator<Operation> CREATOR = new Creator<Operation>() {
			@Override
			public Operation createFromParcel(final Parcel source) {
				return Operation.values()[source.readInt()];
			}

			@Override
			public Operation[] newArray(final int size) {
				return new Operation[size];
			}
		};
	}

	public final static Operation[] OperationVals = Operation.values();

	/**
	   The caller references the constants using <tt>Consts.EMPTY_STRING</tt>, 
	   and so on. Thus, the caller should be prevented from constructing objects of 
	   this class, by declaring this private constructor. 
	 */
	private Constants(){
		//this prevents even the native class from 
		//calling this ctor as well :
		throw new AssertionError();
	}
}
