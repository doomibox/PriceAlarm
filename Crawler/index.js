const chromium = require('chrome-aws-lambda');

exports.handler = async (event, context) => {
  console.log("Event:", event);

  let result = [];
  let browser = null;

  try {
    browser = await chromium.puppeteer.launch({
      args: chromium.args,
      defaultViewport: chromium.defaultViewport,
      executablePath: await chromium.executablePath,
      headless: chromium.headless,
    });

    for (let record of event.Records) {
      let data = JSON.parse(record.body);
      console.log("Start Crawl Job:", data.name, "at", data.url);

      let page = await browser.newPage();
      await page.goto(data.url);
      let title = await page.title();
      console.log("Page title for", data.url, "is", title);
      let price = await page.$eval('span.sale-price-only[itemprop="price"]', span => span.textContent);
      console.log("Merchant Price:", price);
      result.push( {url:data.url, price:price, title:title} );
    }
  } catch (error) {
    return context.fail(error);
  } finally {
    if (browser !== null) {
      await browser.close();
    }
  }
  return JSON.stringify(result);
};