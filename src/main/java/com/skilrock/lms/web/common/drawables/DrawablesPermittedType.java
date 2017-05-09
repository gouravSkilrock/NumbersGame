package com.skilrock.lms.web.common.drawables;

public enum DrawablesPermittedType {
		column(1),line(2),pie(3);
		private int status;
		private DrawablesPermittedType(int c) {
			status = c;
		}
		public int getStatus() {
			return status;
		}
		public static boolean contains(String sts) {
			try {
				DrawablesPermittedType.valueOf(sts);
				return true;
			} catch (Exception e) {
				return false;
			}
		}
	}

