import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Stack;

public class Subway {
    public static void main(String[] args) {

        String fileName = args[0];

        Hashtable<String, String> numToStation = new Hashtable();
        Hashtable<String, Integer> numToArrnum = new Hashtable();
        Hashtable<String, ArrayList<String>> stationToNumSet = new Hashtable<>();
        ArrayList<ArrayList<Integer>> edge = new ArrayList<>();
        ArrayList<String> arrnumToStation = new ArrayList<>();

        try { // chatgpt : 파일 입출력 참고
            FileReader fileReader = new FileReader(fileName);
            BufferedReader bufferedReader = new BufferedReader(fileReader);

            String line;

            int stationCount = 0;
            ArrayList<String> repeated = new ArrayList<>();

            String[] readed;

            while ((line = bufferedReader.readLine())!=null && !line.isEmpty()) { //chatgpt : blank line 입력 관련 조건 참고
                readed = line.split(" ");
                numToStation.put(readed[0], readed[1]);
                numToArrnum.put(readed[0], stationCount);
                ArrayList<String> newNumList = new ArrayList<>();
                if(stationToNumSet.get(readed[1])==null){
                    newNumList.add(readed[0]);
                    stationToNumSet.put(readed[1], newNumList);
                }
                else{
                    if(!repeated.contains(readed[1])) repeated.add(readed[1]);
                    newNumList = stationToNumSet.get(readed[1]);
                    newNumList.add(readed[0]);
                    stationToNumSet.replace(readed[1], newNumList);
                }
                arrnumToStation.add(readed[1]);
                stationCount++;
            }

            for (int i = 0; i < stationCount; i++) {//chatgpt : initialize arrayList by nxn
                edge.add(new ArrayList<>(stationCount));
            }

            for (int i = 0; i < stationCount; i++) {
                ArrayList<Integer> row = edge.get(i);
                for (int j = 0; j < stationCount; j++) {
                    row.add(INF);
                }
            }//chatgpt

            while ((line = bufferedReader.readLine())!=null && !line.isEmpty()){
                readed = line.split(" ");
                edge.get(numToArrnum.get(readed[0])).set(numToArrnum.get(readed[1]), Integer.parseInt(readed[2]));
            }

            for(int i=0; i<repeated.size(); i++){
                ArrayList<String> repeatedStationNum = stationToNumSet.get(repeated.get(i));
                for(int j=0; j<repeatedStationNum.size(); j++){
                    for(int k=0; k<repeatedStationNum.size(); k++){
                        if(j!=k){
                            edge.get(numToArrnum.get(repeatedStationNum.get(j))).set(numToArrnum.get(repeatedStationNum.get(k)), 5);
                        }
                    }
                }
            }

            while ((line = bufferedReader.readLine())!=null && !line.isEmpty()){
                readed = line.split(" ");
                ArrayList<String> repeatedStationNum = stationToNumSet.get(readed[0]);
                for(int j=0; j<repeatedStationNum.size(); j++){
                    for(int k=0; k<repeatedStationNum.size(); k++){
                        if(j!=k){
                            edge.get(numToArrnum.get(repeatedStationNum.get(j))).set(numToArrnum.get(repeatedStationNum.get(k)), Integer.parseInt(readed[1]));
                        }
                    }
                }
            }
            bufferedReader.close();
        } catch (IOException e) {
            System.out.println("can't read file");
        }

        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));




        while (true) { // 과제 5 입력 부분 참고
            try
            {
                String input = br.readLine();
                if (input.compareTo("QUIT") == 0) break;
                else dijkstra(input, edge, arrnumToStation, numToStation, numToArrnum, stationToNumSet);
            }
            catch (IOException e)
            {
                System.out.println("입력이 잘못되었습니다. 오류 : " + e.toString());
            }

        }
    }
    private static final int INF = Integer.MAX_VALUE; // chatgpt : dijkstra 구현 과정에서 참고

    private static void dijkstra(String input, ArrayList<ArrayList<Integer>> edge, ArrayList<String> arrnumToStation, Hashtable numToStation, Hashtable<String, Integer> numToArrnum, Hashtable<String, ArrayList<String>> stationToNumSet){
        //chatgpt : write code for dijkstra and modified it
        String start, end;
        String[] navigate = input.split(" ");
        start = navigate[0];
        end = navigate[1];
        int[] prev = new int[edge.size()];
        boolean[] visited = new boolean[edge.size()];
        int[] distance = new int[edge.size()];
        for(int i=0; i< edge.size(); i++){
            distance[i] = INF;
        }

        for(int i = 0; i<stationToNumSet.get(start).size(); i++) {
            distance[numToArrnum.get(stationToNumSet.get(start).get(i))] = 0;
        }
        for (int i = 0; i < edge.size() - 1; i++) {
            int minVertex = findMinVertex(distance, visited);
            visited[minVertex] = true;

            for (int j = 0; j < edge.size(); j++) {
                if (!visited[j] && edge.get(minVertex).get(j) != INF && distance[minVertex] != INF) {
                    int newDistance = distance[minVertex] + edge.get(minVertex).get(j);
                    if (newDistance < distance[j]) {
                        distance[j] = newDistance;
                        prev[j] = minVertex;
                    }
                }
            }
        }
        int shortDist = INF;
        int endNum = INF;

        for(int i=0; i<stationToNumSet.get(end).size(); i++){
            if(distance[numToArrnum.get(stationToNumSet.get(end).get(i))]<shortDist){
                shortDist = distance[numToArrnum.get(stationToNumSet.get(end).get(i))];
                endNum = numToArrnum.get(stationToNumSet.get(end).get(i));
            }
        }

        Stack<String> path = new Stack<>();

        while(!arrnumToStation.get(endNum).equals(start)){
            path.push(arrnumToStation.get(endNum));
            endNum = prev[endNum];
        }
        path.push(start);

        StringBuilder output = new StringBuilder();
        String curr = "";

        while(!path.isEmpty()){
            curr = path.pop();
            if(path.isEmpty()){
                output.append(curr);
            }
            else if(curr.equals(path.peek())){
                path.pop();
                if(path.isEmpty()) output.append("[").append(curr).append("]");
                else output.append("[").append(curr).append("]").append(" ");
            }
            else{
                output.append(curr).append(" ");
            }
        }

        System.out.print(output.toString()+"\r\n");
        System.out.print(shortDist+"\r\n");
        //chatgpt : write code for dijkstra and modified it

    }
    private static int findMinVertex(int[] distance, boolean[] visited) { //chatgpt : write some code for dijkstra
        int minVertex = -1;
        for (int i = 0; i < distance.length; i++) {
            if (!visited[i] && (minVertex == -1 || distance[i] < distance[minVertex])) {
                minVertex = i;
            }
        }
        return minVertex;
    }
}

