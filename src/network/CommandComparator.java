package network;

import java.util.Comparator;

public class CommandComparator implements Comparator<String> {

	public int compare(String o1, String o2) {
		try {
			if (o1.split("\t")[0].equals("DISCONNECTION")) {
				if (o2.split("\t")[0].equals("DISCONNECTION")) {
					return 0;
				} else {
					return -1;
				}
			} else if (o2.split("\t")[0].equals("DISCONNECTION")) {
				return 1;
			} else {
				long time1 = Long.parseLong(o1.split("[\t]")[1]);
				long time2 = Long.parseLong(o2.split("[\t]")[1]);
				if (time1 < time2) {
					return -1;
				} else if (time1 > time2) {
					return 1;
				} else {
					return 0;
				}
			}
		} catch (IndexOutOfBoundsException e) {
			return 0;
		} catch (NumberFormatException e) {
			return 0;
		}
	}

}
