# PriceAlarm
Merchant prices on shopping websites are constantly changing.
This project tends to track certain merchant prices.

## Development

Mainly there will be three components
- Scheduler
- Crawler
- Web

## Scheduler
- Build JAR: `mvn clean install`
- Build Docker image: `docker build -t pricealarmrepo:LATEST`
- Run: `docker -d -p 8080:8080 pricealarmrepo:LATEST`

## Crawler
- Compress: `zip -r function.zip .`
- Upload to Lambda
