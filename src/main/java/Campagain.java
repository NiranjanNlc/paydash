import com.facebook.ads.sdk.*;

import java.util.Arrays;
import java.util.List;

public class Campagain {
    public static final String ACCESS_TOKEN =  "EAAGbtsIL7M8BACkjn0vnF1rDWpdSY7WBmSkWjxw5VdZCcu9iN1UCDn9OFdb7qJwF60R4YSptDJfE8G4xWjIbIu7ZB3a9D3UGk3iZCRM2xynx1smqcjWuSJsS59gBqzC0fdqW2FxHGhfWAAjeY896qMoDU1TuccRz0S0yGpXLUtQTEMXN2NxPNX1j5AAwlwFa5ZClu6Tu4wZDZD";
    public static final String ACCOUNT_ID = "318153082200920" ;
    public static final String APP_SECRET ="277bb3ac914300a4c9c51e80b936475f" ;
    public static final String CAMPAIGN_ID = "23843629838130426";

    public static final APIContext context = new APIContext(ACCESS_TOKEN, APP_SECRET);
    public Campaign campaign ;

    public void  createCampagain(String name)
    {
        try {
            AdAccount account = new AdAccount(ACCOUNT_ID, context);
            System.out.println(context.toString());
            Campaign campaign = account.createCampaign()
                    .setName(name)
                    .setObjective(Campaign.EnumObjective.VALUE_LINK_CLICKS)
                    .setSpendCap(10000L)
                    .setStatus(Campaign.EnumStatus.VALUE_PAUSED)
                    .execute();
            System.out.println(campaign.fetch());
            batchRequest(account);
        } catch (APIException e) {
            e.printStackTrace();
        }
    }
     public void getCampagainObject()
     {
         try {
             Campaign campaign = Campaign.fetchById(CAMPAIGN_ID, context);
             String id = campaign.getFieldId();
             String name = campaign.getFieldName();
             System.out.println("Campagain id \n"+id+"name "+name);
         } catch (APIException e) {
             e.printStackTrace();
         }
     }
      public void update()   {

          try {
              campaign = Campaign.fetchById(CAMPAIGN_ID, context);
              campaign.update()
                      .setName("Updated Java SDK Test Campaign") // set parameter for the API call
                      .execute();
          } catch (APIException e) {
              e.printStackTrace();
          }

      }
      public void campagainIterator()
      {
          AdAccount account = new AdAccount(ACCOUNT_ID, context);
          APINodeList<Campaign> campaigns = null;
          try {
              campaigns = account.getCampaigns().requestAllFields().execute();
          } catch (APIException e) {
              e.printStackTrace();
          }
          for(Campaign campaign : campaigns) {
              System.out.println(campaign.getFieldName());
          }
      }


public  void batchRequest(AdAccount account)
{
    BatchRequest batch = new BatchRequest(context);
    account.createCampaign()
            .setName("Java SDK Batch Test Campaign")
            .setObjective(Campaign.EnumObjective.VALUE_LINK_CLICKS)
            .setSpendCap(10000L)
            .setStatus(Campaign.EnumStatus.VALUE_PAUSED)
            .addToBatch(batch, "campaignRequest");
    account.createAdSet()
            .setName("Java SDK Batch Test AdSet")
            .setCampaignId("{result=campaignRequest:$.id}")
            .setStatus(AdSet.EnumStatus.VALUE_PAUSED)
            .setBillingEvent(AdSet.EnumBillingEvent.VALUE_IMPRESSIONS)
            .setDailyBudget(1000L)
            .setBidAmount(100L)
            .setOptimizationGoal(AdSet.EnumOptimizationGoal.VALUE_IMPRESSIONS)

            .addToBatch(batch, "adsetRequest");
    account.createAdImage()

            .addToBatch(batch, "imageRequest");
    account.createAdCreative()
            .setTitle("Java SDK Batch Test Creative")
            .setBody("Java SDK Batch Test Creative")
            .setImageHash("{result=imageRequest:$.images.*.hash}")
            .setLinkUrl("www.facebook.com")
            .setObjectUrl("www.facebook.com")
            .addToBatch(batch, "creativeRequest");
    account.createAd()
            .setName("Java SDK Batch Test ad")
            .setAdsetId("{result=adsetRequest:$.id}")
            .setCreative("{creative_id:{result=creativeRequest:$.id}}")
            .setStatus("PAUSED")
            .setBidAmount(100L)
            .addToBatch(batch);
    try {
        List<APIResponse> responses = batch.execute();
    } catch (APIException e) {
        e.printStackTrace();
    }
}
        public void getEdgecampagain()
        {
            try {
                APINodeList<Campaign> campaigns = new AdAccount(ACCOUNT_ID, context).getCampaigns()
                        .setEffectiveStatus(Arrays.asList(Campaign.EnumEffectiveStatus.VALUE_ACTIVE,Campaign.EnumEffectiveStatus.VALUE_PAUSED))
                        .requestNameField()
                        .requestObjectiveField()
                        .execute();
                for(Campaign campaign : campaigns) {
                    System.out.println(campaign.getFieldName());
                }

            } catch (APIException e) {
                e.printStackTrace();
            }
        }
}
