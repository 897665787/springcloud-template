package com.company.framework.sequence.db;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.company.framework.sequence.SequenceGenerator;

public class DBGenerator implements SequenceGenerator {
	
//	@Autowired
//	private ProcDao procDao;

//	@Override
//	@Transactional(propagation = Propagation.NOT_SUPPORTED)
//	public String getSequence(String name, int digitNum) {
//		Sequence sequence = new Sequence(name);
//		procDao.nextLongValue(sequence);
//		Long seqvalue = sequence.getValue();
//		if (seqvalue == null) {
//			return null;
//		}
//		return String.format("%0" + digitNum + "d", seqvalue);
//	}
//	
//	@Override
//	@Transactional(propagation = Propagation.NOT_SUPPORTED)
//	public String getSequence(String name) {
//		Sequence sequence = new Sequence(name);
//		procDao.nextLongValue(sequence);
//		Long seqvalue = sequence.getValue();
//		if (seqvalue == null) {
//			return null;
//		}
//		return String.valueOf(seqvalue);
//	}

	@Override
	@Transactional(propagation = Propagation.NOT_SUPPORTED)
	public long nextId() {
		return 0;
	}

}