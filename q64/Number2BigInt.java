package q64;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Number2BigInt {

	public static void main(String[] args) {
		byte[] digits;
		byte[] oDigits;
		int trailingZero = 0;
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		String str = null;
		try {
			str = br.readLine();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		int[] cdigits = str.chars().map(c -> c -= '0').toArray();
		int size = cdigits.length;
		for (int z = cdigits.length - 1; z >= 0; z--) {
			if (cdigits[z] == 0) {
				size--;
				trailingZero++;
			} else {
				break;
			}
		}
		digits = new byte[size];
		oDigits = new byte[size];
		for (int i = 0; i < size; i++) {
			digits[i] = (byte) cdigits[i];
		}
		System.arraycopy(digits, 0, oDigits, 0, digits.length);
		System.out.println("Digits: " + digits.length);
		long now = System.nanoTime();
		Operation[] values = { Operation.ADD, Operation.SUBTRACT, Operation.MULTIPLY };
		int n = values.length;
		for (int o = 0; o < 4; o++) {
			int i[] = new int[digits.length];
			int rc = 0;
			int jmax = 1;
			for (int jt = 0; jt < digits.length; jt++) {
				jmax *= n;
			}
			for (int j = 0; j < jmax; j++) {
				rc = 0;
				int current = values[i[0]] == Operation.MULTIPLY ? 1 : 0;
				while (rc < digits.length) {
					Operation cop = values[i[rc]];
					current = cop.process(current, digits[rc]);
					rc++;
				}
				if (current == 3) {
					Operation[] data = new Operation[digits.length];
					for (int z = 0; z < digits.length; z++) {
						data[z] = values[i[z]];
					}
					//------------------
					System.out.println("After " + o + " iterations coverage is done! (Time: " + ((System.nanoTime() - now) / Double.valueOf(1000000)) + "ms)");
					StringBuilder text = new StringBuilder("");
					int current2 = data[0] == Operation.MULTIPLY ? 1 : 0;
					int prn2 = 0;
					for (int m = 0; m < digits.length; m++) {
						Operation crnOp = data[m];
						current2 = crnOp.process(current2, digits[m]);
						if (crnOp == Operation.MULTIPLY) {
							text.append(")");
							prn2++;
						}
						if (m == 0 && (crnOp == Operation.MULTIPLY || crnOp == Operation.ADD)) {
							text.append(digits[m]);
						} else {
							text.append(crnOp.getSign() + digits[m]);
						}
						if (trailingZero > 0) {
							int count = m;
							while (count > 0) {
								count -= oDigits.length;
							}
							if (count == 0) {
								for (int z = 0; z < trailingZero; z++) {
									text.append("+0");
								}
							}
						}
					}
					for (int m = 0; m < prn2; m++) {
						text.insert(0, "(");
					}
					if (current == 3) {
						System.out.println(text.toString() + "=3");
					} /*else if (BigInteger.valueOf(current) == bnum.add(BigInteger.valueOf(3))) {
						System.out.println(text.toString() + "-" + bnum + "=3");
						} else if (BigInteger.valueOf(current) == bnum.subtract(BigInteger.valueOf(3))) {
						System.out.println(bnum + "-" + text.toString() + "=3");
						} else if (BigInteger.valueOf(current) == bnum.multiply(BigInteger.valueOf(-1)).add(BigInteger.valueOf(3))) {
						System.out.println(text.toString() + "+" + bnum + "=3");
						}*/
					exit();
					System.exit(0);
					return;
					//------------------
				}
				rc = 0;
				while (rc < digits.length) {
					if (i[rc] < n - 1) {
						i[rc]++;
						break;
					} else {
						i[rc] = 0;
					}
					rc++;
				}
			}
			byte[] ndigits = new byte[digits.length + oDigits.length];
			System.arraycopy(digits, 0, ndigits, 0, digits.length);
			System.arraycopy(oDigits, 0, ndigits, digits.length, oDigits.length);
			if (ndigits.length > 100000) {
				System.out.println("No solution!");
				exit();
				System.exit(0);
				return;
			}
			digits = ndigits;
		}
		System.out.println("No solution!");
		exit();
		return;
	}

	private static void exit() {
		System.out.println("Press any key to exit...");
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
			br.readLine();
			br.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static enum Operation {
		ADD("+", true), SUBTRACT("-", true), MULTIPLY("*", true), DIVIDE("/", false), CONST("", false), MOD("%", false), POWER("^", false), BITWISE("&", false), SHIFT_RIGHT(">>", false), SHIFT_LEFT("<<", false), OR("|", false);
		private String sign;
		private boolean use;

		private Operation(String sign, boolean use) {
			this.sign = sign;
			this.use = use;
		}

		public String getSign() {
			return sign;
		}

		public int process(int u, int z) {
			switch (this) {
			case CONST:
				return u;
			case DIVIDE:
				return u / z;
			case SUBTRACT:
				return u - z;
			case MULTIPLY:
				return u * z;
			case MOD:
				return u % z;
			case ADD:
				return u + z;
			case POWER:
				int r = 1;
				for (int i = 0; i < z; i++) {
					r *= u;
				}
				return r;
			case BITWISE:
				return u & z;
			case SHIFT_RIGHT:
				return u >> z;
			case SHIFT_LEFT:
				return u << z;
			case OR:
				return u | z;
			default:
				return 0;
			}
		}

		public boolean use() {
			return use;
		}
	}
}
