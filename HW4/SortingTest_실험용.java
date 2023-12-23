import java.io.*;
import java.util.*;

public class SortingTest
{
	public static void main(String args[])
	{
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

		try
		{
			boolean isRandom = false;	// 입력받은 배열이 난수인가 아닌가?
			int[] value;	// 입력 받을 숫자들의 배열
			String nums = br.readLine();	// 첫 줄을 입력 받음
			if (nums.charAt(0) == 'r')
			{
				// 난수일 경우
				isRandom = true;	// 난수임을 표시

				String[] nums_arg = nums.split(" ");

				int numsize = Integer.parseInt(nums_arg[1]);	// 총 갯수
				int rminimum = Integer.parseInt(nums_arg[2]);	// 최소값
				int rmaximum = Integer.parseInt(nums_arg[3]);	// 최대값

				Random rand = new Random();	// 난수 인스턴스를 생성한다.

				value = new int[numsize];	// 배열을 생성한다.
				for (int i = 0; i < value.length; i++)	// 각각의 배열에 난수를 생성하여 대입
					value[i] = rand.nextInt(rmaximum - rminimum + 1) + rminimum;
			}
			else
			{
				// 난수가 아닐 경우
				int numsize = Integer.parseInt(nums);

				value = new int[numsize];	// 배열을 생성한다.
				for (int i = 0; i < value.length; i++)	// 한줄씩 입력받아 배열원소로 대입
					value[i] = Integer.parseInt(br.readLine());
			}

			// 숫자 입력을 다 받았으므로 정렬 방법을 받아 그에 맞는 정렬을 수행한다.
			while (true)
			{
				int[] newvalue = (int[])value.clone();	// 원래 값의 보호를 위해 복사본을 생성한다.
                char algo = ' ';

				if (args.length == 4) {
                    return;
                }

				String command = args.length > 0 ? args[0] : br.readLine();

				if (args.length > 0) {
                    args = new String[4];
                }
				
				long t = System.nanoTime();
				switch (command.charAt(0))
				{
					case 'B':	// Bubble Sort
						newvalue = DoBubbleSort(newvalue);
						break;
					case 'I':	// Insertion Sort
						newvalue = DoInsertionSort(newvalue);
						break;
					case 'H':	// Heap Sort
						newvalue = DoHeapSort(newvalue);
						break;
					case 'M':	// Merge Sort
						newvalue = DoMergeSort(newvalue);
						break;
					case 'Q':	// Quick Sort
						newvalue = DoQuickSort(newvalue);
						break;
					case 'R':	// Radix Sort
						newvalue = DoRadixSort(newvalue);
						break;
					case 'S':	// Search
						algo = DoSearch(newvalue);
						break;
					case 'X':
						return;	// 프로그램을 종료한다.
					default:
						throw new IOException("잘못된 정렬 방법을 입력했습니다.");
				}
				if (isRandom)
				{
					// 난수일 경우 수행시간을 출력한다.
					System.out.println((System.nanoTime() - t) + " ms");
					// 최적 알고리즘 확인.
					if (command.charAt(0) == 'S') {
						System.out.println("Search 결과 : " + algo);
						newvalue = (int[])value.clone();
						DoMergeSort(newvalue);
						newvalue = (int[])value.clone();
						DoMergeSort(newvalue);
						newvalue = (int[])value.clone();
						long a = System.nanoTime();
						DoMergeSort(newvalue);
						System.out.println("Merge : " + (System.nanoTime() - a) + " ms");
						newvalue = (int[])value.clone();
						DoQuickSort(newvalue);
						newvalue = (int[])value.clone();
						DoQuickSort(newvalue);
						newvalue = (int[])value.clone();
						a = System.nanoTime();
						DoQuickSort(newvalue);
						System.out.println("Quick : " + (System.nanoTime() - a) + " ms");
						newvalue = (int[])value.clone();
						DoRadixSort(newvalue);
						newvalue = (int[])value.clone();
						DoRadixSort(newvalue);
						newvalue = (int[])value.clone();
						a = System.nanoTime();
						DoRadixSort(newvalue);
						System.out.println("Radix : " + (System.nanoTime() - a) + " ms");
					}

				}
				else
				{
					// 난수가 아닐 경우 정렬된 결과값을 출력한다.
                    if (command.charAt(0) != 'S') {
                        for (int i = 0; i < newvalue.length; i++) {
                            System.out.println(newvalue[i]);
                        }
                    } else {
                        System.out.println(algo);
						newvalue = (int[])value.clone();
						DoMergeSort(newvalue);
						newvalue = (int[])value.clone();
						DoMergeSort(newvalue);
						newvalue = (int[])value.clone();
						long a = System.nanoTime();
						DoMergeSort(newvalue);
						System.out.println("Merge : " + (System.nanoTime() - a) + " ms");
						newvalue = (int[])value.clone();
						DoQuickSort(newvalue);
						newvalue = (int[])value.clone();
						DoQuickSort(newvalue);
						newvalue = (int[])value.clone();
						a = System.nanoTime();
						DoQuickSort(newvalue);
						System.out.println("Quick : " + (System.nanoTime() - a) + " ms");
						newvalue = (int[])value.clone();
						DoRadixSort(newvalue);
						newvalue = (int[])value.clone();
						DoRadixSort(newvalue);
						newvalue = (int[])value.clone();
						a = System.nanoTime();
						DoRadixSort(newvalue);
						System.out.println("Radix : " + (System.nanoTime() - a) + " ms");
                    }
				}

			}
		}
		catch (IOException e)
		{
			System.out.println("입력이 잘못되었습니다. 오류 : " + e.toString());
		}
	}

	////////////////////////////////////////////////////////////////////////////////////////////////////
	private static int[] DoBubbleSort(int[] value) { // Chatgpt : 버블 정렬 구현
		int n = value.length;
		for (int i = 0; i < n-1; i++) {
			for (int j = 0; j < n-i-1; j++) {
				if (value[j] > value[j+1]) {
					int temp = value[j];
					value[j] = value[j+1];
					value[j+1] = temp;
				}
			}
		}
		return value;
	}


	////////////////////////////////////////////////////////////////////////////////////////////////////
	private static int[] DoInsertionSort(int[] value) { // Chatgpt : 삽입 정렬 구현
		int n = value.length;
		for (int i = 1; i < n; i++) {
			int key = value[i];
			int j = i - 1;
			while (j >= 0 && value[j] > key) {
				value[j+1] = value[j];
				j--;
			}
			value[j+1] = key;
		}
		return value;
	}


	////////////////////////////////////////////////////////////////////////////////////////////////////
	private static int[] DoHeapSort(int[] value) { // Chatgpt : 힙 정렬 구현
		// max heap 생성
		buildMaxHeap(value);

		// heap을 이용하여 정렬
		for (int i = value.length - 1; i >= 1; i--) {
			// 최대값과 맨 끝의 값을 swap
			swap(value, 0, i);

			// heap size를 줄여서 heap 조건 유지
			maxHeapify(value, 0, i);
		}

		return value;
	}

	// 최대 힙을 만드는 함수
	private static void buildMaxHeap(int[] value) {
		// leaf 노드들을 제외한 나머지 노드에 대하여 max heapify 수행
		for (int i = value.length / 2 - 1; i >= 0; i--) {
			maxHeapify(value, i, value.length);
		}
	}

	// 최대 힙 유지 함수
	private static void maxHeapify(int[] value, int i, int heapSize) {
		int left = 2 * i + 1;
		int right = 2 * i + 2;
		int largest = i;

		// 왼쪽 자식이 부모보다 크면 largest를 왼쪽 자식으로 업데이트
		if (left < heapSize && value[left] > value[largest]) {
			largest = left;
		}

		// 오른쪽 자식이 부모보다 크면 largest를 오른쪽 자식으로 업데이트
		if (right < heapSize && value[right] > value[largest]) {
			largest = right;
		}

		// largest가 i가 아니면 i와 largest를 swap하고, max heapify 수행
		if (largest != i) {
			swap(value, i, largest);
			maxHeapify(value, largest, heapSize);
		}
	}

	// 두 인덱스의 값을 서로 교환하는 함수
	private static void swap(int[] value, int i, int j) {
		int temp = value[i];
		value[i] = value[j];
		value[j] = temp;
	}


	////////////////////////////////////////////////////////////////////////////////////////////////////
	private static int[] DoMergeSort(int[] value){ //2021-중간고사 5번 문제 아이디어 참고하여 코드 작성
		int[] A = new int[value.length]; //성능 향상을 위해 switching merge 형태로 구현하였음
		for(int i=0; i< value.length; i++){
			A[i] = value[i];
		}
		MergeSort(0, value.length-1, value, A);
		return value;
	}

	private static void MergeSort(int p, int r, int[] A, int[] B) { // Chatgpt : 병합 정렬 구현 후 switching merge sort로 수정
		if (p < r) {
			int q = (p+r)/2;
			MergeSort(p, q, B, A);
			MergeSort(q+1, r, B, A);
			switching_merge(p, q, r, B, A);
		}
	}

	// 두 배열을 합치는 함수
	private static void switching_merge(int left, int mid, int right, int[] A, int[] B){
		int i = left;
		int j = mid+1;
		int t = left;
		while (i <= mid && j <= right) {
			if(A[i] <= A[j]){
				B[t++] = A[i++];
			}
			else{
				B[t++] = A[j++];
			}
		}
		while(i <= mid) {
			B[t++] = A[i++];
		}
		while(j <= right) {
			B[t++] = A[j++];
		}
	}

	////////////////////////////////////////////////////////////////////////////////////////////////////
	static int[] DoQuickSort(int[] value) { // Chatgpt : 퀵 정렬 구현
		quickSort(value, 0, value.length - 1);
		return value;
	}

	private static void quickSort(int[] value, int left, int right) {
		if (left < right) {
			int[] pi = partition(value, left, right);

			quickSort(value, left, pi[0] - 1);
			quickSort(value, pi[1] + 1, right);
		}
	}

	// Chatgpt : 중복된 원소에 대해 성능 높이기 위해 3-way partition 구현
	private static int[] partition(int[] value, int left, int right) {
		int pivot = value[left];
		int i = left + 1;
		int lt = left;
		int gt = right;

		while (i <= gt) {
			if (value[i] < pivot) {
				swap(value, lt++, i++);
			} else if (value[i] > pivot) {
				swap(value, i, gt--);
			} else {
				i++;
			}
		}

		return new int[] {lt, gt};
	}

	////////////////////////////////////////////////////////////////////////////////////////////////////
	static int[] DoRadixSort(int[] arr) { // Chatgpt : radix sort 구현
		int[] positive = new int[arr.length];
		int[] negative = new int[arr.length];

		int posCount = 0, negCount = 0;

		for (int i : arr) {
			if (i < 0) {
				negative[negCount++] = -(i + 1); // store as positive and add 1 before negating
			} else {
				positive[posCount++] = i;
			}
		}

		// sort each array
		if (posCount > 0) radixSort(positive, posCount);
		if (negCount > 0) radixSort(negative, negCount);

		// combine sorted arrays
		for (int i = 0; i < negCount; i++) {
			arr[i] = -(negative[negCount - 1 - i] + 1); // reverse order, make negative and subtract 1
		}
		for (int i = 0; i < posCount; i++) {
			arr[negCount + i] = positive[i];
		}

		return arr;
	}

	private static void radixSort(int[] arr, int length) {
		int max = getMax(arr, length);
		int[] count = new int[10]; // Reuse the count array

		for (int exp = 1; max / exp > 0; exp *= 10) {
			// Reset count array
			for (int i = 0; i < 10; i++) {
				count[i] = 0;
			}
			countSort(arr, length, count, exp);
		}
	}

	private static int getMax(int[] arr, int length) {
		int max = arr[0];
		for (int i = 1; i < length; i++) {
			if (arr[i] > max) {
				max = arr[i];
			}
		}
		return max;
	}

	private static void countSort(int[] arr, int length, int[] count, int exp) {
		int[] output = new int[length];

		for (int i = 0; i < length; i++) {
			count[(arr[i] / exp) % 10]++;
		}

		for (int i = 1; i < 10; i++) {
			count[i] += count[i - 1];
		}

		for (int i = length - 1; i >= 0; i--) {
			output[--count[(arr[i] / exp) % 10]] = arr[i];
		}

		for (int i = 0; i < length; i++) {
			arr[i] = output[i];
		}
	}

	////////////////////////////////////////////////////////////////////////////////////////////////////
    private static char DoSearch(int[] value) // Chatgpt : docx에 있는 search 알고리즘에 대한 아이디어를 구현
	{
		double radixMaxDigits = 3;
		double duplicateThreshold = 0.993;
		double sortedThreshold = 0.999;
		System.out.println("MaxDigit = " + getMaxDigit(value) + " and radixMaxDigits = " + radixMaxDigits);
		System.out.println("CollisionRate = " + getCollisionRate(value));
		System.out.println("SortedRate = " + getSortedRate(value));

		long t = System.currentTimeMillis();

		if (getSortedRate(value) >= sortedThreshold) {
			DoInsertionSort(value);
			System.out.println(System.currentTimeMillis()-t + "ms");
			return 'I';
		}

		if (getMaxDigit(value) <= radixMaxDigits) {
			if(value.length>=1000){
				DoRadixSort(value);
				return 'R';
			}
			else if(getCollisionRate(value) >= 0.89){
				DoQuickSort(value);
				return 'Q';
			}
			else{
				DoMergeSort(value);
				return 'M';
			}
		}

		if (getCollisionRate(value) >= duplicateThreshold) {
			DoQuickSort(value);
			return 'Q';
		}

		else return 'M';// 조건이 충족되지 않을 경우, 기본적으로 Merge Sort 추천
	}


	private static int getMaxDigit(int[] value){ // Chatgpt
		int[] positive = new int[value.length];
		int[] negative = new int[value.length];

		int posCount = 0, negCount = 0;

		for (int i : value) {
			if (i < 0) {
				negative[negCount++] = i;
			} else {
				positive[posCount++] = i;
			}
		}
		int max = getMax(positive, posCount);
		int min = getMin(negative, negCount);

		int maxDigitCount = 0;
		int minDigitCount = 0;

		while(max>0){
			maxDigitCount++;
			max = max/10;
		}
		while(min<0){
			minDigitCount++;
			min = min/10;
		}
		if(maxDigitCount>minDigitCount) return maxDigitCount;
		else return minDigitCount;
	}

	private static int getMin(int[] arr, int length) { // Chatgpt
		int min = arr[0];
		for (int i = 1; i < length; i++) {
			if (arr[i] < min) {
				min = arr[i];
			}
		}
		return min;
	}

	private static double getCollisionRate(int[] arr) { // Chatgpt
		HashSet<Integer> set = new HashSet<>();
		int collisionCount = 0;
		for (int num : arr) {
			if (!set.add(num)) {
				collisionCount++;
			}
		}
		return (double) collisionCount / arr.length;
	}

	private static double getSortedRate(int[] arr) { // Chatgpt
		int sortedPairsCount = 0;
		for (int i = 0; i < arr.length - 1; i++) {
			if (arr[i] <= arr[i + 1]) {
				sortedPairsCount++;
			}
		}
		return (double)sortedPairsCount / (arr.length - 1);
	}
}
