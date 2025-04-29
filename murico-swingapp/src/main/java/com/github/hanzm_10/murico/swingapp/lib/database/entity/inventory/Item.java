package com.github.hanzm_10.murico.swingapp.lib.database.entity.inventory;

import java.sql.Timestamp;

import org.jetbrains.annotations.NotNull;

import com.github.hanzm_10.murico.swingapp.lib.database.entity.user.UserGender;
import com.github.hanzm_10.murico.swingapp.lib.database.entity.user.User.Builder;

public record Item(int _itemId, Timestamp itemCreatedAt, String itemName, int itemMinQuantity, float itemSRP, float itemWSP, int itemCategoryId, int itemPackTypeId ) {

	public static class Builder{
		private int itemId;
		private Timestamp itemCreatedAt;
		private String itemName;
		private int itemCategId;
		private int itemMinQty;
		private float itemSRP;
		private float itemWSP;
		private int itemPckTypeId;
		
		public @NotNull Item build() throws IllegalStateException{
			if (itemId <= 0) {
                throw new IllegalStateException("Item ID must be greater than 0");
            }
			if (itemCreatedAt == null) {
                throw new IllegalStateException("Item creation time cannot be null");
            }
			if (itemName == null || itemName.isBlank()) {
                throw new IllegalStateException("Display name cannot be null or empty");
            }
			return new Item(itemId, itemCreatedAt, itemName, itemMinQty, itemSRP, itemWSP, itemCategId, itemPckTypeId );
		}
		 public Builder setItemCreatedAt(Timestamp itemCreatedAt) {
	            this.itemCreatedAt = itemCreatedAt;
	            return this;
	        }

	        public Builder setItemName(String itemName) {
	            this.itemName = itemName;
	            return this;
	        }

	        public Builder setItemSrp(float itemSRP) {
	            this.itemSRP = itemSRP;
	            return this;
	        }
	        public Builder setItemWsp(float itemWSP) {
	            this.itemWSP = itemWSP;
	            return this;
	        }

	        public Builder setItemMinQty(int itemMinQty) {
	            this.itemMinQty = itemMinQty;
	            return this;
	        }

	        public Builder setItemId(int itemId) {
	            this.itemId = itemId;
	            return this;
	        }
	        public Builder setItemCatId(int itemCategId) {
	            this.itemCategId = itemCategId;
	            return this;
	        }
	        public Builder setItemPckId(int itemPckTypeId) {
	            this.itemPckTypeId = itemPckTypeId;
	            return this;
	        }
		
	}
}
