package eg.edu.alexu.csd.filestructure.btree.cs23_cs24_cs39;

import eg.edu.alexu.csd.filestructure.btree.ISearchResult;

public class SearchResult implements ISearchResult {
	
	public SearchResult(String num , int freq) {
		this.Id = num;
		this.rank=freq;
	}
	
	public SearchResult() {
					}
	private String Id ;
	private int rank;

	@Override
	public String getId() {
		return this.Id;
	}

	@Override
	public void setId(String id) {
		this.Id = id;

	}

	@Override
	public int getRank() {
		return this.rank;
	}

	@Override
	public void setRank(int rank) {
		this.rank= rank;

	}

}
