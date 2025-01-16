import java.time.LocalDateTime;

public class AuctionSystemDemo {
    public static void main(String[] args) throws InterruptedException {
        AuctionSystem auctionSystem = AuctionSystem.getInstance();

        // Create Users
        User seller = new User("Seller", "seller@example.com");
        User bidder1 = new User("Bidder1", "bidder1@example.com");
        User bidder2 = new User("Bidder2", "bidder2@example.com");
        User bidder3 = new User("Bidder3", "bidder3@example.com");

        // Add money to bidders' wallets
        bidder1.addMoneyInWallet(1000);
        bidder2.addMoneyInWallet(1500);
        bidder3.addMoneyInWallet(500);

        // Create Items
        Item item1 = new Item("Vintage Watch");
        Item item2 = new Item("Antique Vase");

        // Create Auction Events
        AuctionEvent auction1 = new AuctionEvent(
                "Auction for Vintage Watch",
                10, // Duration in seconds
                100, // Starting price
                LocalDateTime.now(),
                item1,
                seller
        );

        AuctionEvent auction2 = new AuctionEvent(
                "Auction for Antique Vase",
                20, // Duration in seconds
                200, // Starting price
                LocalDateTime.now(),
                item2,
                seller
        );

        // Register Auctions
        auctionSystem.createAuction(auction1);
        auctionSystem.createAuction(auction2);

        // Place Bids
        System.out.println("\n** Placing Bids **");
        auctionSystem.placeBid(auction1, bidder1, 150);
        auctionSystem.placeBid(auction1, bidder2, 200);
        auctionSystem.placeBid(auction1, bidder3, 300); // Should fail (insufficient wallet balance)
        auctionSystem.placeBid(auction2, bidder1, 250);
        auctionSystem.placeBid(auction2, bidder3, 300);

        // Search Auctions
        System.out.println("\n** Searching Auctions **");
        auctionSystem.searchAuction("Antique").forEach(auction ->
                System.out.println("Found Auction: " + auction.getName())
        );

        // Extend Auction Duration
        System.out.println("\n** Extending Auction Duration **");
        auction1.extendDuration(5);

        // Wait for Auction to End
        System.out.println("\n** Waiting for Auction to End **");
        Thread.sleep(15000); // Sleep to let auctions close automatically

        // Print Results
        System.out.println("\n** Auction Results **");
        System.out.println("Auction 1 Status: " + auction1.getAuctionStatus());
        System.out.println("Highest Bidder for Auction 1: " +
                (auction1.getHighestBidder() != null ? auction1.getHighestBidder().getName() : "None"));
        System.out.println("Auction 2 Status: " + auction2.getAuctionStatus());
        System.out.println("Highest Bidder for Auction 2: " +
                (auction2.getHighestBidder() != null ? auction2.getHighestBidder().getName() : "None"));

        // Shutdown Scheduler
        auctionSystem.shutdownScheduler();
    }
}
