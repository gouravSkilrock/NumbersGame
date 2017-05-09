package com.skilrock.itg.IDBarcode;

public class IntHashtableEntry {

	int hash;

	int key;

	IntHashtableEntry next;
	Object value;

	IntHashtableEntry() {
	}

	@Override
	protected Object clone() {
		IntHashtableEntry inthashtableentry = new IntHashtableEntry();
		inthashtableentry.hash = hash;
		inthashtableentry.key = key;
		inthashtableentry.value = value;
		inthashtableentry.next = next != null ? (IntHashtableEntry) next
				.clone() : null;
		return inthashtableentry;
	}
}