import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArrayList;

public class AuctionEvent {
    private final String id;
    private final String name;
    private final Item item;
    private final User seller;
    private User highestBidder;
    private long currentBid;
    private int duration; // seconds
    private long startingPrice;
    private final LocalDateTime startingTime;
    private final List<User> bidders;
    private AuctionStatus auctionStatus;

    public AuctionEvent(final String name, final int duration,
                         final long startingPrice, final LocalDateTime startingTime, final Item item, final User seller) {

        if (duration<=0 || startingPrice<0) {
            throw new IllegalArgumentException("Duration and price must be greater than 0");
        }
        this.id = UUID.randomUUID().toString();
        this.name = name;
        this.duration = duration;
        this.startingPrice = startingPrice;
        this.startingTime = startingTime;
        this.currentBid = startingPrice;
        this.item = item;
        this.seller = seller;
        this.highestBidder = null;
        bidders = new CopyOnWriteArrayList<>();
        this.auctionStatus = AuctionStatus.ACTIVE;
    }

    public synchronized void placeBid(final User user, final long bid) {
        if (auctionStatus==AuctionStatus.CLOSED || bid <= currentBid || !user.isAbleToBid(bid)) {
            System.out.println("Bid rejected for user: " + user.getName());
            return;
        }

        if (highestBidder!=null) {
            highestBidder.removePotentialAuction(currentBid);
        }

        currentBid = bid;
        highestBidder = user;
        user.addPotentialAuction(currentBid);
        bidders.add(user);
        System.out.println("Bid placed for user" + user.getName());
//        notifyobserver()
    }

    public synchronized void closeAuction() {
        if (auctionStatus == AuctionStatus.CLOSED) {
            System.out.println("Auction is already closed");
            return;
        }

        auctionStatus = AuctionStatus.CLOSED;

        if (highestBidder!= null) {
            highestBidder.addWinAuction(id);
            highestBidder.removePotentialAuction(currentBid);
            highestBidder.deductMoneyFromWallet(currentBid);
            System.out.println("Auction won by: " + highestBidder.getName() + " for amount: " + currentBid);
        } else {
            System.out.println("No bid for item is placed, closing the auction");
        }
    }

    public synchronized void extendDuration(int additionalDuration) {
        if (auctionStatus == AuctionStatus.CLOSED) {
            System.out.println("Cannot extended duration. Auction closed");
            return;
        }

        duration += additionalDuration;
        System.out.println("Auction duration extended by " + additionalDuration + " seconds.");
    }

    public synchronized void setStartingPrice(long startingPrice) {
        if (LocalDateTime.now().isAfter(startingTime)) {
            System.out.println("Auction has started, cannot modify the starting price");
            return;
        }
        this.startingPrice = startingPrice;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public User getHighestBidder() {
        return highestBidder;
    }

    public long getCurrentBid() {
        return currentBid;
    }

    public int getDuration() {
        return duration;
    }

    public long getStartingPrice() {
        return startingPrice;
    }

    public LocalDateTime getStartingTime() {
        return startingTime;
    }

    public List<User> getBidders() {
        return bidders;
    }

    public Item getItem() {
        return item;
    }

    public User getSeller() {
        return seller;
    }

    public AuctionStatus getAuctionStatus() {
        return auctionStatus;
    }
}
