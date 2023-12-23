import java.io.*;
import java.util.ArrayList;

public class Matching
{
	public static void main(String args[])
	{
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

		while (true)
		{
			try
			{
				String input = br.readLine();
				if (input.compareTo("QUIT") == 0)
					break;

				command(input);
			}
			catch (IOException e)
			{
				System.out.println("입력이 잘못되었습니다. 오류 : " + e.toString());
			}
		}
	}

	private static void command(String input)
	{
		char oper = input.charAt(0);
		switch (oper){
			case '<':
				database = new Hashtable();
				linecnt = 0;
				String filePath = input.substring(2);
				try (FileReader fileReader = new FileReader(filePath);
					 BufferedReader bufferedReader = new BufferedReader(fileReader)) {
					stringArray = readFileToStringArray(filePath);
					String line;
					String substring;
					while ((line = bufferedReader.readLine()) != null) {
						linecnt++;
						for(int j=0; j<line.length()-5; j++){
							substring = line.substring(j, j+6);
							database.insert(substring, linecnt, j+1);
						}
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
				break;
			case '@':
				String index = input.substring(2);
				int indexNum;
				if(index.length()==1){
					indexNum = index.charAt(0)-'0';
				}
				else{
					indexNum = (index.charAt(0)-'0')*10 + (index.charAt(1)-'0');
				}
				database.stringSearch(indexNum);
				break;
			case '?':
				String pattern = input.substring(2);
				LinkedList<int[]>[] next = new LinkedList[pattern.length()-5];
				String output = "";
				for(int i=0; i<pattern.length()-5; i++) {
					next[i] = database.patternSearch(pattern.substring(i, i + 6)).list;
				}
				if(pattern.length()==6){
					for(int i=0; i<next[0].size; i++){
						output += "(" + next[0].get(i)[0] + ", " + next[0].get(i)[1] + ") ";
					}
				}
				else {
					boolean exist = true;
					for (int i = 0; i < next[0].size(); i++) {
						for (int j = 1; j < next.length; j++) {
							exist = false;
							for (int k = 0; k < next[j].size(); k++) {
								if (next[j].get(k)[0] == next[0].get(i)[0] && next[j].get(k)[1] == next[0].get(i)[1] + j) {
									exist = true;
									break;
								}
							}
							if (!exist) break;
						}
						if (exist) {
							output += "(" + next[0].get(i)[0] + ", " + next[0].get(i)[1] + ") ";
						}
					}
				}
				if (!output.isEmpty()) {
					output = output.substring(0, output.length() - 1);
					System.out.println(output);
				}
				else{
					System.out.println("(0, 0)");
				}
				break;
			case '/':
				ArrayList<ArrayList<Integer>> pos = new ArrayList<>(linecnt);
				for(int i=0; i<linecnt; i++){
					pos.add(new ArrayList<>());
				}
				LinkedList toDelete = database.patternSearch(input.substring(2)).list;
				System.out.println(toDelete.size);
				for(LinkedList.Node<int[]> node = toDelete.head; node!=null; node=node.next){
					for(int i=0; i<6; i++) {
						if (pos.get(node.data[0]-1).isEmpty() || !pos.get(node.data[0] - 1).contains(node.data[1] - 1 + i)) {
							pos.get(node.data[0] - 1).add(node.data[1] - 1 + i);
						}
					}
				}
				for(int i=0; i<linecnt; i++){
					StringBuilder stringBuilder = new StringBuilder();
					for(int j=0; j<stringArray[i].length(); j++){
						if(!pos.get(i).contains(j)){
							stringBuilder.append(stringArray[i].charAt(j));
						}
					}
					stringArray[i] = stringBuilder.toString();
				}
				database = new Hashtable();
				String substring;
				for(int i=0; i<linecnt; i++){
					for(int j=0; j<stringArray[i].length()-5; j++){
						substring = stringArray[i].substring(j, j+6);
						database.insert(substring, linecnt, j+1);
					}
				}
				break;
			case '+':
				linecnt++;
				String toAdd = input.substring(2);
				for(int i=0; i<toAdd.length()-5; i++){
					substring = toAdd.substring(i, i+6);
					database.insert(substring, linecnt, i+1);
				}
				System.out.println(linecnt);
				break;
			default:
				System.out.println("incorrect operatioin");
		}
	}

	private static String[] readFileToStringArray(String filePath) { //Chatgpt 파일 내용을 읽어와 문자열 배열로 저장하기 위한 함수 작성
		StringBuilder stringBuilder = new StringBuilder();
		try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
			String line;
			while ((line = reader.readLine()) != null) {
				stringBuilder.append(line).append("\n");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		String content = stringBuilder.toString();
		String[] stringArray = content.split("\n");

		return stringArray;
	}
	private static Hashtable database = new Hashtable();
	private static String[] stringArray;
	private static int linecnt = 0;

	private static class Hashtable{ // 강의 자료 예시 코드 참고해 작성
		private AVLTree[] table;
		private Hashtable(){
			table = new AVLTree[100];
			for(int i=0; i<100; i++){
				table[i] = new AVLTree();
			}
		}
		private int hash(String s){
			int cnt = 0;
			for(int i=0; i<s.length(); i++){
				cnt += s.charAt(i);
			}
			return cnt%100;
		}

		private void insert(String s, int a, int b){
			int slot;
			slot = hash(s);
			table[slot].insert(s, a, b);
		}

		private void stringSearch(int a){
			AVLTree slot = table[a];
			if(slot.isEmpty()){
				System.out.println("EMPTY");
			}
			else{
				slot.getStrings();
			}
		}

		private AVLNode patternSearch(String pattern){
			return table[hash(pattern)].search(pattern);
		}


	}

	private static class AVLNode{ //강의 자료 참고해 작성
		private LinkedList<int[]> list;
		private String s;
		private AVLNode left, right;
		private int height;
		public AVLNode(String substring){
			s = substring;
			left = right = AVLTree.NIL;
			height = 1;
			list = new LinkedList<>();
		}

		public AVLNode(String substring, AVLNode leftChild, AVLNode rightChild, int h){
			s = substring;
			left = leftChild;
			right = rightChild;
			height = h;
			list = new LinkedList<>();
		}
	}


	private static class AVLTree{ //강의 자료 참고해 작성 후 수정
		private AVLNode root;
		static final AVLNode NIL = new AVLNode(null, null, null, 0);
		private AVLTree(){
			root = NIL;
		}

		private boolean isEmpty(){
			if(root==NIL){
				return true;
			}
			else return false;
		}

		private void getStrings(){
			System.out.println(preorder(root));
		}
		private String preorder(AVLNode node) { //chatgpt preorder 알고리즘 참고
			if (node != NIL){
				if(node.left != NIL && node.right != NIL) return node.s + " " + preorder(node.left) + " " + preorder(node.right);
				else if(node.left == NIL && node.right != NIL) return node.s + " " + preorder(node.right);
				else if(node.left != NIL && node.right == NIL) return node.s + " " + preorder(node.left);
				else return node.s;
			}

			else return "";
		}
		private AVLNode search(String substring){
			return searchItem(root, substring);
		}

		private AVLNode searchItem(AVLNode tNode, String substring){
			if (tNode == NIL) return NIL;
			else if(substring.compareTo(tNode.s) == 0) return tNode;
			else if(substring.compareTo(tNode.s)<0){
				return searchItem(tNode.left, substring);
			}
			else return searchItem(tNode.right, substring);
		}

		private void insert(String substring, int a, int b){
			root = insertItem(root, substring, a, b);
		}

		private AVLNode insertItem(AVLNode tNode, String substring, int a, int b){
			int type;
			if(tNode == NIL) {
				tNode = new AVLNode(substring);
				tNode.list.add(new int[]{a, b});
			}
			else if(substring.compareTo(tNode.s)<0){
				tNode.left = insertItem(tNode.left, substring, a, b);
				tNode.height = 1 + Math.max(tNode.right.height, tNode.left.height);
				type = needBalance(tNode);
				if(type != NO_NEED){
					tNode = balanceAVL(tNode, type);
				}
			}
			else if(substring.compareTo(tNode.s)>0){
				tNode.right = insertItem(tNode.right, substring, a, b);
				tNode.height = 1 + Math.max(tNode.right.height, tNode.left.height);
				type = needBalance(tNode);
				if(type != NO_NEED){
					tNode = balanceAVL(tNode, type);
				}
			}
			else{
				tNode.list.add(new int[]{a, b});
			}
			return tNode;
		}

		private void delete(String substring){
			root = findAndDelete(root, substring);
		}
		private AVLNode findAndDelete(AVLNode tNode, String substring){
			if (tNode == NIL) return NIL;
			else{
				if(substring.compareTo(tNode.s)==0){
					tNode = deleteNode(tNode);
				}
				else if(substring.compareTo(tNode.s)<0){
					tNode.left = findAndDelete(tNode.left, substring);
					tNode.height = 1 + Math.max(tNode.right.height, tNode.left.height);
					int type = needBalance(tNode);
					if(type != NO_NEED){
						tNode = balanceAVL(tNode, type);
					}
				}
				else{
					tNode.right = findAndDelete(tNode.right, substring);
					tNode.height = 1 + Math.max(tNode.right.height, tNode.left.height);
					int type = needBalance(tNode);
					if(type != NO_NEED){
						tNode = balanceAVL(tNode, type);
					}
				}
				return tNode;
			}
		}

		private AVLNode deleteNode(AVLNode tNode){
			if((tNode.left == NIL)&&(tNode.right==NIL)){
				return NIL;
			}
			else if(tNode.left==NIL){
				return tNode.right;
			}
			else if(tNode.right==NIL){
				return tNode.left;
			}
			else{
				returnPair rPair = deleteMinItem(tNode.right);
				tNode.s = rPair.substring;
				tNode.right = rPair.node;
				tNode.height = 1 + Math.max(tNode.right.height, tNode.left.height);
				int type = needBalance(tNode);
				if(type != NO_NEED){
					tNode = balanceAVL(tNode, type);
				}
				return tNode;
			}
		}

		private returnPair deleteMinItem(AVLNode tNode){
			if(tNode.left == NIL){
				return new returnPair(tNode.s, tNode.right);
			}
			else{
				returnPair rPair = deleteMinItem(tNode.left);
				tNode.left = rPair.node;
				tNode.height = 1 + Math.max(tNode.right.height, tNode.left.height);
				int type = needBalance(tNode);
				if(type != NO_NEED){
					tNode = balanceAVL(tNode, type);
				}
				rPair.node = tNode;
				return rPair;
			}
		}

		private class returnPair{
			String substring;
			AVLNode node;
			private returnPair(String s, AVLNode nd){
				substring = s;
				node = nd;
			}
		}

		private final int LL=1, LR=2, RR=3, RL=4, NO_NEED=0, ILLEGAL=-1;
		private int needBalance(AVLNode t){
			int type = ILLEGAL;
			if(t.left.height+2<=t.right.height){
				if(t.right.left.height<=t.right.right.height){
					type = RR;
				}
				else{
					type = RL;
				}
			}
			else if(t.left.height>=t.right.height+2){
				if(t.left.left.height>=t.left.right.height){
					type = LL;
				}
				else{
					type = LR;
				}
			}
			else type = NO_NEED;
			return type;
		}

		private AVLNode balanceAVL(AVLNode tNode, int type){
			AVLNode returnNode = NIL;
			switch (type){
				case LL:
					returnNode = rightRotate(tNode);
					break;
				case LR:
					tNode.left = leftRotate(tNode.left);
					returnNode = rightRotate(tNode);
					break;
				case RR:
					returnNode = leftRotate(tNode);
					break;
				case RL:
					tNode.right = rightRotate(tNode.right);
					returnNode = leftRotate(tNode);
					break;
				default:
					System.out.println("Impossible type");
					break;
			}
			return returnNode;
		}

		private AVLNode leftRotate(AVLNode t){
			AVLNode RChild = t.right;
			AVLNode RLChild = RChild.left;
			RChild.left = t;
			t.right = RLChild;
			t.height = 1 + Math.max(t.left.height, t.right.height);
			RChild.height = 1 + Math.max(RChild.left.height, RChild.right.height);
			return RChild;
		}

		private AVLNode rightRotate(AVLNode t){
			AVLNode LChild = t.left;
			AVLNode LRChild = LChild.right;
			LChild.right = t;
			t.left = LRChild;
			t.height = 1 + Math.max(t.left.height, t.right.height);
			LChild.height = 1 + Math.max(LChild.left.height, LChild.right.height);
			return LChild;
		}
	}

	private static class LinkedList<T> { //Chatgpt linked list 구현 참고
		private Node<T> head;
		private int size;

		private static class Node<T> {
			T data;
			Node<T> next;

			Node(T data) {
				this.data = data;
				this.next = null;
			}
		}

		private void add(T data) {
			Node<T> newNode = new Node<>(data);

			if (head == null) {
				head = newNode;
			} else {
				Node<T> current = head;
				while (current.next != null) {
					current = current.next;
				}
				current.next = newNode;
			}

			size++;
		}

		private T get(int index) {
			if (index < 0 || index >= size) {
				throw new IndexOutOfBoundsException();
			}

			Node<T> current = head;
			for (int i = 0; i < index; i++) {
				current = current.next;
			}

			return current.data;
		}

		private int size() {
			return size;
		}
	}

}

