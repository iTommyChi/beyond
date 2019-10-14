package cn.duan.generator;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Random;

/**
 * 模拟数据程序
 * @author Administrator
 *
 */
public class GenerateMockData {

	/**
	 * 模拟数据
	 */
	public static void main(String[] args) {
		BufferedWriter bw = null;

		/**
		 * ==================================================================
		 */
		try {
			String file = "D://user.csv";
			bw = new BufferedWriter(new OutputStreamWriter(
					new FileOutputStream(file, true)));

			Random random = new Random();
			String[] sexes = new String[]{"male", "female"};
			String[] citys = new String[]{"ShangHai", "BeiJing","Hangzhou","GuangZhou","JieYang","FuZhou","ChongQin","TianJin"};
			for (int i = 0; i < 1000000; i++) {
				long userid = i;
				//String username = "user" + i;
				String name = "name" + i;
				int age = random.nextInt(60);
				//String professional = "professional" + random.nextInt(100);
				//String city = "city" + random.nextInt(100);
				String city = citys[random.nextInt(8)];
				String sex = sexes[random.nextInt(2)];

//				bw.write(userid + "\t"  + name + "\t" + age +
//						"\t" +  city + "\t" + sex + "\n");
				bw.write(userid + ","  + name + "," + age +
						"," +  city + "," + sex + "\n");
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				bw.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}
	
}
