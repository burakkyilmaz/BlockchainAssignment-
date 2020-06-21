package com.packagename.myapp.service.kriptoblock;

import java.util.List;

import com.packagename.myapp.model.KriptoBlock;

public interface KriptoBlockService {

	public KriptoBlock lastBlock();

	public KriptoBlock newBlock(String data);

	public void addBlock(KriptoBlock kriptoBlock);

	public Boolean validateFirstBlock();

	public Boolean validateNewBlock(KriptoBlock newBlock, KriptoBlock previousBlock);

	public Boolean validateBlockchain();

	public void mineBlock(KriptoBlock block);

	public void setDifficulty(Integer difficulty);

	List<KriptoBlock> getBlokList();
}
