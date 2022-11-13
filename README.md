# promotionengine
Generate the total price by applying promotion type, where each promotion type can be added through configuration.
We can add as many as promotion types for calculating the value of orders.

# PromotionEngineContainer
PromotionEngineContainer class load the configuration and create the list of promotion types.
It create PromotionEngineThread and worker tread.
Worker thread recive the order from transport and pass it PormotionEngineThread through blockEing queue.
PromotionEngineThread process each received order and calculate the total price after applying all the promotion types. 

# AppMainService
AppMainService creates PromotionEngineContainer and register the PricePublisher interface to publish calculated total price for each order.

Code improvement:
1) Used blocking queue, but code can be improved by using lock less queue.
2) Allocating memory of OrderInfoHolder for each order request, but it can be improved by using object pool.

