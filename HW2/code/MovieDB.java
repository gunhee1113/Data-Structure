import java.util.Iterator;

/**
 * Genre, Title 을 관리하는 영화 데이터베이스.
 * 
 * MyLinkedList 를 사용해 각각 Genre와 Title에 따라 내부적으로 정렬된 상태를  
 * 유지하는 데이터베이스이다. 
 */
public class MovieDB {
	MyLinkedList<Genre> genreList;
    public MovieDB() {
        // FIXME implement this
    	genreList = new MyLinkedList<Genre>();
    	// HINT: MovieDBGenre 클래스를 정렬된 상태로 유지하기 위한 
    	// MyLinkedList 타입의 멤버 변수를 초기화 한다.
    }

    public void insert(MovieDBItem item) {
        // FIXME implement this
        // Insert the given item to the MovieDB.
		MyLinkedListIterator<Genre> genreIterator = new MyLinkedListIterator(genreList);
		if(!genreIterator.hasNext()){
			genreList.add(new Genre(item.getGenre()));
			genreList.head.getNext().getItem().movieLists.add(item.getTitle());
			return;
		}
		Genre nextGenre;
		Node<Genre> curr = genreList.head;
		while(genreIterator.hasNext()){
			nextGenre = genreIterator.next();
			if(nextGenre.getItem().compareTo(item.getGenre())>0){
				curr.insertNext(new Genre(item.getGenre()));
				genreList.numItems++;
				curr.getNext().getItem().movieLists.add(item.getTitle());
				return;
			}
			else if(nextGenre.getItem().compareTo(item.getGenre())==0){
				Iterator<String> movieIterator = nextGenre.movieLists.iterator();
				String nextMovie;
				Node<String> curr_movie = nextGenre.movieLists.movieItems.head;
				while(movieIterator.hasNext()){
					nextMovie = movieIterator.next();
					if(nextMovie.compareTo(item.getTitle())>0){
						curr_movie.insertNext(item.getTitle());
						nextGenre.movieLists.movieItems.numItems++;
						return;
					}
					else if(nextMovie.compareTo(item.getTitle())==0){
						return;
					}
					curr_movie = curr_movie.getNext();
				}
				curr_movie.insertNext(item.getTitle());
				nextGenre.movieLists.movieItems.numItems++;
				return;
			}
			curr = curr.getNext();
		}
		curr.insertNext(new Genre(item.getGenre()));
		genreList.numItems++;
		curr.getNext().getItem().movieLists.add(item.getTitle());
    }

    public void delete(MovieDBItem item) {
        // FIXME implement this
        // Remove the given item from the MovieDB.
		MyLinkedListIterator<Genre> genreIterator = new MyLinkedListIterator(genreList);
		if(!genreIterator.hasNext()){
			return;
		}
		Genre nextGenre;
		Node<Genre> curr = genreList.head;
		while(genreIterator.hasNext()){
			nextGenre = genreIterator.next();
			if(nextGenre.getItem().compareTo(item.getGenre())==0){
				Iterator<String> movieIterator = nextGenre.movieLists.iterator();
				String nextMovie;
				Node<String> curr_movie = nextGenre.movieLists.movieItems.head;
				while(movieIterator.hasNext()){
					nextMovie = movieIterator.next();
					if(nextMovie.compareTo(item.getTitle())==0){
						curr_movie.removeNext();
						if(nextGenre.movieLists.movieItems.isEmpty()){
							curr.removeNext();
						}
						break;
					}
					curr_movie = curr_movie.getNext();
				}
				break;
			}
			curr = curr.getNext();
		}
    }

    public MyLinkedList<MovieDBItem> search(String term) {
        // FIXME implement this
        // Search the given term from the MovieDB.
        // You should return a linked list of MovieDBItem.
        // The search command is handled at SearchCmd class.
		MyLinkedList<MovieDBItem> results = new MyLinkedList<MovieDBItem>();
		MyLinkedListIterator<Genre> genreIterator = new MyLinkedListIterator(genreList);
		Genre nextGenre;
		while(genreIterator.hasNext()){
			nextGenre = genreIterator.next();
			Iterator<String> movieIterator = nextGenre.movieLists.iterator();
			String nextMovie;
			while(movieIterator.hasNext()){
				nextMovie = movieIterator.next();
				if(nextMovie.contains(term)){
					results.add(new MovieDBItem(nextGenre.getItem(), nextMovie));
				}
			}
		}
    	// Printing search results is the responsibility of SearchCmd class. 
    	// So you must not use System.out in this method to achieve specs of the assignment.
    	
        // This tracing functionality is provided for the sake of debugging.
        // This code should be removed before submitting your work.
    	// FIXME remove this code and return an appropriate MyLinkedList<MovieDBItem> instance.
    	// This code is supplied for avoiding compilation error.

        return results;
    }
    
    public MyLinkedList<MovieDBItem> items() {
        // FIXME implement this
        // Search the given term from the MovieDatabase.
        // You should return a linked list of QueryResult.
        // The print command is handled at PrintCmd class.
		MyLinkedList<MovieDBItem> results = new MyLinkedList<MovieDBItem>();
		MyLinkedListIterator<Genre> genreIterator = new MyLinkedListIterator(genreList);
		Genre nextGenre;
		while(genreIterator.hasNext()){
			nextGenre = genreIterator.next();
			Iterator<String> movieIterator = nextGenre.movieLists.iterator();
			String nextMovie;
			while(movieIterator.hasNext()){
				nextMovie = movieIterator.next();
				results.add(new MovieDBItem(nextGenre.getItem(), nextMovie));
			}
		}
        
    	return results;
    }
}

class Genre extends Node<String> implements Comparable<Genre> {
	MovieList movieLists;
	public Genre(String name) {
		super(name);
		movieLists = new MovieList();
	}
	
	@Override
	public int compareTo(Genre o) {
		return this.getItem().compareTo(o.getItem());
	}

	@Override
	public int hashCode() {
		throw new UnsupportedOperationException("not implemented yet");
	}

	@Override
	public boolean equals(Object obj) {
		return this.getItem()==obj;
	}
}

class MovieList implements ListInterface<String> {
	MyLinkedList<String> movieItems;
	public MovieList() {
		movieItems = new MyLinkedList<String>();
	}

	@Override
	public Iterator<String> iterator() {
		return new MyLinkedListIterator<String>(movieItems);
	}

	@Override
	public boolean isEmpty() {
		return movieItems.isEmpty();
	}

	@Override
	public int size() {
		return movieItems.size();
	}

	@Override
	public void add(String item) {
		movieItems.add(item);
	}

	@Override
	public String first() {
		return movieItems.first();
	}

	@Override
	public void removeAll() {
		throw new UnsupportedOperationException("not implemented yet");
	}
}