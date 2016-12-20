import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Vector;

/**
 * 
 * @author lxw.
 *
 */
public class VsmMain {
	
	HashMap<Integer, Double> resultMap = new HashMap<Integer, Double>();

	public static void main(String[] args) {
		VsmMain vsm = new VsmMain();
		vsm.vsm();
	}

	public void vsm() {
		String basePath;
		int i = 0;
		while (i < 20) {
			i++;
			try {
				basePath = new File("").getCanonicalPath().toString() + "\\res\\";
				String content = getContent(basePath + i + ".txt");
				Vector<Vector<String>> samples = loadSample(basePath + "sort.txt");
				samilarity(content, samples, i);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		Double[] list = new Double[20];
		int t=0;
		for (Integer item : resultMap.keySet()) {
			list[t++] = resultMap.get(item);
		}
		Arrays.sort(list);
		
		while (t > 0) {
			t--;
			for (Integer x : resultMap.keySet()) {
				if (resultMap.get(x) == list[t]) {
//					System.out.println(x);
//					System.out.println(list[t]);
					System.out.println(x + " : " + list[t]);
				}
			}
		}
	}
	
	/**
	 * 计算比对文章和样本的余弦值
	 * 
	 * @param content
	 * @param samples
	 */
	public void samilarity(String content, Vector<Vector<String>> samples, int arg) {
		for (int i = 0; i < samples.size(); i++) {
			Vector<String> single = samples.get(i);
			// 存放每个样本中的词语，在该对比文本中出现的次数
			Vector<Integer> wordCount = new Vector<Integer>();
			for (int j = 0; j < single.size(); j++) {
				String word = single.get(j);
				int count = getCharInStringCount(content, word);
				wordCount.add(j, count);
				//System.out.print(word + ":" + tfidf + ",");
			}
			//System.out.println("\n");
			// 计算余弦值
			int sampleLength = 0;
			int textLength = 0;
			int totalLength = 0;
			for (int j = 0; j < single.size(); j++) {
				// 样本中向量值都是1
				sampleLength += 1;
				textLength += wordCount.get(j) * wordCount.get(j);
				totalLength += 1 * wordCount.get(j);
			}
			// 开方计算
			double value = 0.00;
			if(sampleLength > 0 && textLength > 0){
				value = (double)totalLength/(Math.sqrt(sampleLength) * Math.sqrt(textLength));
			}
			
//			System.out.println(single.get(0) + "," + sampleLength + ","
//					+ textLength + "," + totalLength + "," + value);
			resultMap.put(arg, value);
		}
	}

	/**
	 * 计算word在content中出现的次数
	 * 
	 * @param content
	 * @param word
	 * @return
	 */
	public int getCharInStringCount(String content, String word) {
		String str = content.replaceAll(word, "");
		return (content.length() - str.length()) / word.length();

	}

	/**
	 * 加载样本
	 * 
	 * @param path
	 * @return
	 */
	public Vector<Vector<String>> loadSample(String path) {
		Vector<Vector<String>> vector = new Vector<Vector<String>>();
		try {
			try {
				FileReader reader = new FileReader(new File(path));
				BufferedReader bufferReader = new BufferedReader(reader);
				String hasRead = "";
				while ((hasRead = bufferReader.readLine()) != null) {
					String info[] = hasRead.split(",");
					Vector<String> single = new Vector<String>();
					for (int i = 0; i < info.length; i++) {
						single.add(info[i]);
					}
					vector.add(single);
				}
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return vector;
	}

	/**
	 * 读取对应path的文件内容
	 * 
	 * @param path
	 * @return
	 */
	public String getContent(String path) {
		StringBuffer buffer = new StringBuffer();
		try {
			try {
				FileReader reader = new FileReader(new File(path));
				BufferedReader bufferReader = new BufferedReader(reader);
				String hasRead = "";
				while ((hasRead = bufferReader.readLine()) != null) {
					buffer.append(hasRead);
				}
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return buffer.toString();
	}

}
