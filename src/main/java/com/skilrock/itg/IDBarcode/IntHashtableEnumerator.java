package com.skilrock.itg.IDBarcode;

import java.util.Enumeration;
import java.util.NoSuchElementException;

// Referenced classes of package com.businessrefinery.barcode.linear.encoder:
//            IntHashtableEntry

public class IntHashtableEnumerator implements Enumeration {

	IntHashtableEntry entry;

	int index;

	boolean keys;

	IntHashtableEntry table[];

	IntHashtableEnumerator(IntHashtableEntry ainthashtableentry[], boolean flag) {
		table = ainthashtableentry;
		keys = flag;
		index = ainthashtableentry.length;
	}

	public boolean hasMoreElements() {
		if (entry != null) {
			return true;
		}
		while (index-- > 0) {
			if ((entry = table[index]) != null) {
				return true;
			}
		}
		return false;
	}

	public Object nextElement() {
		if (entry == null) {
			while (index-- > 0 && (entry = table[index]) == null) {
				;
			}
		}
		if (entry != null) {
			IntHashtableEntry inthashtableentry = entry;
			entry = inthashtableentry.next;
			return keys ? new Integer(inthashtableentry.key)
					: inthashtableentry.value;
		} else {
			throw new NoSuchElementException("IntHashtableEnumerator");
		}
	}
}