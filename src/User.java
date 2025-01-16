import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

public class User {
    private final String id;
    private final String name;
    private final String email;
    private long wallet;
    private long totalBidAmount;
    private List<String> allAuctionWins;

    public User(final String name, final String email) {
        this.id = UUID.randomUUID().toString();
        this.name = name;
        this.email = email;
        this.allAuctionWins = new CopyOnWriteArrayList<>();
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public void addMoneyInWallet(final long amount) {
        wallet += amount;
    }

    public boolean isAbleToBid(final long amount) {
        return amount <= wallet-totalBidAmount;
    }

    public void removePotentialAuction(final long amount) {
        totalBidAmount -= amount;
    }

    public void addPotentialAuction(final long bid) {
        totalBidAmount += bid;
    }

    public void addWinAuction(final String auctionEvent) {
        allAuctionWins.add(auctionEvent);
    }

    public void deductMoneyFromWallet(final long amount) {
        wallet -= amount;
    }



}
