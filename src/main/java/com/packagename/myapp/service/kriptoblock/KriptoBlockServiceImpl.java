package com.packagename.myapp.service.kriptoblock;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.packagename.myapp.model.KriptoBlock;
import com.packagename.myapp.service.hashservice.HashService;

@Service
public class KriptoBlockServiceImpl implements KriptoBlockService {

	@Autowired
	HashService hashService;

	private int difficulty;
	private List<KriptoBlock> blocks = new ArrayList<>();

	@Override
	public void setDifficulty(Integer difficulty) {
		this.difficulty = difficulty;
		addBlock(createFirstBlock());
	}

	private KriptoBlock createFirstBlock() {
		KriptoBlock kriptoBlockFirst = new KriptoBlock();
		kriptoBlockFirst.setIndex(0);
		kriptoBlockFirst.setCurrentTimes(System.currentTimeMillis());
		kriptoBlockFirst.setNonce(null);
		kriptoBlockFirst.setPreviousHash(null);
		kriptoBlockFirst.setValue("");
		kriptoBlockFirst.setHash(hashService.getHashKey(kriptoBlockFirst.getConcatFields()));

		return kriptoBlockFirst;
	}

	@Override
	public KriptoBlock lastBlock() {
		return blocks.get(blocks.size() - 1);
	}

	@Override
	public KriptoBlock newBlock(String data) {

		KriptoBlock kriptoBlock = new KriptoBlock();
		kriptoBlock.setIndex(lastBlock().getIndex() + 1);
		kriptoBlock.setCurrentTimes(System.currentTimeMillis());
		kriptoBlock.setNonce(0);
		kriptoBlock.setPreviousHash(lastBlock().getHash());
		kriptoBlock.setValue(data);

		return kriptoBlock;
	}

	@Override
	public void addBlock(KriptoBlock kriptoBlock) {
		if (kriptoBlock != null) {
			kriptoBlock.setHash(hashService.getHashKey(kriptoBlock.getConcatFields()));
			mineBlock(kriptoBlock);
			blocks.add(kriptoBlock);
		}
	}

	@Override
	public Boolean validateFirstBlock() {
		KriptoBlock firstBlock = blocks.get(0);

		if (firstBlock.getIndex() != 0 || firstBlock.getPreviousHash() != null || firstBlock.getHash() == null
				|| !hashService.getHashKey(firstBlock.getConcatFields()).equals(firstBlock.getHash())) {
			return false;
		}

		return true;
	}

	@Override
	public Boolean validateNewBlock(KriptoBlock newBlock, KriptoBlock previousBlock) {
		if (newBlock != null && previousBlock != null) {

			if (newBlock.getPreviousHash() == null || !newBlock.getPreviousHash().equals(previousBlock.getHash())) {
				return false;
			}

			if (newBlock.getHash() == null || !hashService.getHashKey(newBlock.getConcatFields()).equals(newBlock.getHash())) {
				return false;
			}

			return true;
		}

		return false;
	}

	@Override
	public Boolean validateBlockchain() {
		if (!validateFirstBlock()) {
			return false;
		}

		for (int i = 1; i < blocks.size(); i++) {
			KriptoBlock currentBlock = blocks.get(i);
			KriptoBlock previousBlock = blocks.get(i - 1);

			if (!validateNewBlock(currentBlock, previousBlock)) {
				return false;
			}
		}

		return true;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Blockchain [difficulty=");
		builder.append(difficulty);
		builder.append(", blocks=");
		builder.append("\n");
		for (KriptoBlock block : blocks) {
			builder.append(block);
			builder.append("\n");
		}
		builder.append("]");
		return builder.toString();
	}

	@Override
	public void mineBlock(KriptoBlock block) {
		block.setNonce(0);
		StringBuilder builder = new StringBuilder();
		for (int i = 0; i < difficulty; i++) {
			builder.append('0');
		}
		String zeros = builder.toString();
		while (!block.getHash().substring(0, difficulty).equals(zeros)) {
			block.setNonce(block.getNonce() + 1);
			block.setHash(hashService.getHashKey(block.getConcatFields()));
		}

	}

	@Override
	public List<KriptoBlock> getBlokList() {
		return blocks;
	}

}
