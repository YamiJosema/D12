package services;

import java.util.Calendar;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import repositories.BidRepository;
import security.Authority;
import security.LoginService;
import security.UserAccount;
import domain.Bid;
import domain.Bidder;
import domain.Concept;

@Service
@Transactional
public class BidService {

	//Managed repository
	@Autowired
	private BidRepository	bidRepository;


	//Validator
//	@Autowired
//	private Validator validator;
	
	//Supporting services

	//Constructors
	public BidService() {
		super();
	}

	//Simple CRUD methods
	public Bid create(Bidder bidder, Concept concept) {
		Assert.notNull(bidder);
		Assert.notNull(concept);
		Bid res;
		res = new Bid();
		res.setBidder(bidder);
		res.setConcept(concept);
		res.setMoment(Calendar.getInstance().getTime());
		return res;
	}

	public Collection<Bid> findAll() {
		final Collection<Bid> res = this.bidRepository.findAll();
		return res;
	}

	public Bid findOne(final int bidId) {
		final Bid res = this.bidRepository.findOne(bidId);
		return res;
	}

	public Bid save(final Bid bid) {
		Assert.notNull(bid, "The bid to save cannot be null.");
		final UserAccount ua = LoginService.getPrincipal();
		Assert.notNull(ua);
		final Authority a = new Authority();
		a.setAuthority(Authority.BIDDER);
		Assert.isTrue(ua.getAuthorities().contains(a), "You must to be a bidder to create a bid.");
		
		final Bid res = this.bidRepository.save(bid);
		return res;
	}

	public void delete(final Bid bid) {
		final UserAccount ua = LoginService.getPrincipal();
		Assert.notNull(ua);
		final Authority a = new Authority();
		a.setAuthority(Authority.BIDDER);
		Assert.isTrue(ua.getAuthorities().contains(a), "You must to be a bidder to delete a bid.");

		Assert.notNull(bid, "The bid to delete cannot be null.");
		Assert.isTrue(this.bidRepository.exists(bid.getId()));
		
		this.bidRepository.delete(bid);
	}

	//Utilites methods

}